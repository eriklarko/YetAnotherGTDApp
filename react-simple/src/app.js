import { AnotherComponent } from "./another-component.js"

class SomeComponent extends React.Component {
    render() {
        return <div>
            I am SomeComponent
            <AnotherComponent />
        </div>
    }
}

ReactDOM.render(
  <SomeComponent />,
  document.getElementById('app')
);

