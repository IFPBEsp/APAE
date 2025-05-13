"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.Button = Button;
var jsx_runtime_1 = require("react/jsx-runtime");
function Button(props) {
    return (0, jsx_runtime_1.jsx)("button", { onClick: function () { return props.onClick(); }, children: props.children });
}
exports.default = Button;
