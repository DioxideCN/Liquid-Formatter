#include <jni.h>
#include <pdh.h>
#include <vector>
#include <thread>
#include "run_halo_liquid_utils_CPUWatchDog.h"

extern "C" {
#include <pthread.h>
}

int getNumberOfCores()
{
    SYSTEM_INFO sysInfo;
    GetSystemInfo(&sysInfo);
    return sysInfo.dwNumberOfProcessors;
}

// 封装获取单个CPU使用率函数 -> cpuIndex索引
double getSingleCpuUsePercentage(int cpuIndex)
{
    PDH_HQUERY query;
    PDH_HCOUNTER counter;
    wchar_t counterPath[256];

    swprintf_s(counterPath, sizeof(counterPath), L"\\Processor(%d)\\%% Processor Time", cpuIndex);
    PdhOpenQuery(nullptr, 0, &query);
    PdhAddCounterW(query, counterPath, 0, &counter);
    PdhCollectQueryData(query);
    Sleep(1000);
    PdhCollectQueryData(query);
    PDH_FMT_COUNTERVALUE pdhValue;
    DWORD dwValue;
    PdhGetFormattedCounterValue(counter, PDH_FMT_DOUBLE, &dwValue, &pdhValue);
    PdhCloseQuery(query);
    return pdhValue.doubleValue;
}

// 获取单个CPU使用率
JNIEXPORT jdouble JNICALL Java_run_halo_liquid_utils_CPUWatchDog_getCPULoad
  (JNIEnv *env, jobject obj, jint cpuIndex)
{
    return getSingleCpuUsePercentage(cpuIndex);
}

void *getSingleCpuUsePercentageWrapper(void *args) {
    int core = *((int*)args);
    double load = getSingleCpuUsePercentage(core);
    return (void*)(new double(load));
}

// 获取所有CPU使用率
JNIEXPORT jdoubleArray JNICALL Java_run_halo_liquid_utils_CPUWatchDog_getBatchCPULoad
  (JNIEnv *env, jobject obj)
{
    int SIZE = getNumberOfCores();
    // 创建一个长度为SIZE的Java数组double[]
    jdoubleArray arrayArray = env->NewDoubleArray(SIZE);
    jdouble *elements = env->GetDoubleArrayElements(arrayArray, 0);
    std::vector<pthread_t> threads(SIZE);
    std::vector<int> cores(SIZE);
    for (int i = 0; i < SIZE; i++) {
        cores[i] = i;
        pthread_create(&threads[i], nullptr, getSingleCpuUsePercentageWrapper, &cores[i]);
    }

    for (int i = 0; i < SIZE; i++) {
        double *result;
        pthread_join(threads[i], (void**)&result);
        elements[i] = *result;
        delete result;
    }

    env->ReleaseDoubleArrayElements(arrayArray, elements, 0);
    return arrayArray;
}
