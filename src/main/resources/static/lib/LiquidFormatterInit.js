(() => {
    document.addEventListener("DOMContentLoaded", () => {
        const container = document.createElement("div");
        const root = document.createElement("div");
        const styleEl = document.createElement("script");
        const shadowDOM = container.attachShadow?.({ mode: "open" }) || container;
        styleEl.setAttribute(
            "src",
            "/plugins/LiquidFormatter/assets/static/lib/GraphHandler.js"
        );
        shadowDOM.appendChild(styleEl);
        shadowDOM.appendChild(root);
        document.body.appendChild(container);
    });
})();