(function() {
    // 用来分隔不同的 G2.Chart 对象
    let regexChart = /((?:const|let|var)?\s*[^=]*?=\s*new\s*G2.Chart[\s\S]*?}\);)([\s\S]*?)(?=(?:const|let|var)?\s*[^=]*?=\s*new\s*G2.Chart|$)/g;
    // 用来获取 container 的名称
    let regExp = /container: '([^']*)',/;
    const elements = document.querySelectorAll('code.language-g2');
    elements.forEach(element => {
        let text = element.textContent;
        let newText = text.replace(regexChart, function(match, chartStr, chartOps) {
            let matchContainer = regExp.exec(chartStr);
            if (matchContainer !== null) {
                let occupied = document.getElementById(matchContainer[1]).parentNode.clientWidth;
                const g2replacer = chartStr.replace(/\$\{(\(.+\)\(.+\)|occupied)}/g, function(match) {return parseExpression(match, occupied).toString();});
                // 返回处理后的 chart 对象和其对应的操作
                return g2replacer + chartOps;
            }
            return match;  // 如果没有找到匹配项，则返回原字符串
        });
        eval(newText);
        element.parentNode.remove();
    });

    // 用来分隔不同的 X6.Graph 对象
    let regexGraph = /((?:const|let|var)?\s*[^=]*?=\s*new\s*X6.Graph[\s\S]*?\}\)|\;)([\s\S]*?)(?=(?:const|let|var)?\s*[^=]*?=\s*new\s*X6.Graph|$)/g;
    // 用来获取 container 的名称
    let regexContainer = /container: document.getElementById\('([^']*)'\),/;
    $('.prism.language-x6').each(function () {
        let text = $(this).text();
        let newText = text.replace(regexGraph, function(match, graphStr, graphOps) {
            let matchContainer = regexContainer.exec(graphStr);
            if (matchContainer !== null) {
                let occupied = $(`#${matchContainer[1]}`).parent().width();
                const x6replacer = graphStr.replace(/\$\{(\(.+\)\(.+\)|occupied)}/g, function(match) {return parseExpression(match, occupied).toString();});
                // 返回处理后的 graph 对象和其对应的操作
                return x6replacer + graphOps;
            }
            return match;  // 如果没有找到匹配项，则返回原字符串
        });
        eval(newText);
        $(this).parent().remove();
    })


    function parseExpression(expression, occupied) {
        if (expression === "${occupied}") {
            return occupied;
        }

        const match = expression.match(/^\$\{\((<|>|<=|>=)(\d+):(.+)\)\((.+)\)}$/);

        if (match) {
            const operator = match[1];
            const threshold = Number(match[2]);
            const valueIfTrue = match[3] === "occupied" ? occupied : Number(match[3]);
            const valueIfFalse = match[4] === "occupied" ? occupied : Number(match[4]);

            if ((operator === "<" && $(this).width() < threshold) ||
                (operator === ">" && $(this).width() > threshold) ||
                (operator === "<=" && $(this).width() <= threshold) ||
                (operator === ">=" && $(this).width() >= threshold)) {
                return valueIfTrue;
            } else {
                return valueIfFalse;
            }
        }
        throw new Error(`wrong chart size expression "${expression}"`);
    }
})();
