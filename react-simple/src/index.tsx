import * as React from "react";
import * as ReactDOM from "react-dom";
import { Router, Route, hashHistory } from "react-router";

import { Hello } from "./components/Hello";


const App = () => <Hello compiler="TypeScript" framework="React" />
const Bpp = () => <Hello compiler="SHITNE" framework="React" />
const route = (<Router history={hashHistory}>
    <Route path="/" component={App} />
    <Route path="/a" component={Bpp} />
</Router>);

ReactDOM.render(
    route,
    document.getElementById("example")
);
