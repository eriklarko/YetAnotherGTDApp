import * as systemjs from "systemjs";
import "../config"

import * as Promise from "promise";
import * as sinon from "sinon";
import * as chai from "chai";
var assert = chai.assert;

describe('Route store', () =>  {
	let routeStore = null;
	let dispatcher = null;
	beforeEach(function() {
		return Promise.all([
			systemjs.import('./src/stores/route-store'),
			systemjs.import('./src/dispatcher')
		]).then(function(modules) {
			routeStore = new modules[0].routeStore.constructor();
			dispatcher = modules[1].dispatcher;
		});
	});

	it('default state is undefined', () => {
		var defaultState = routeStore.getCurrentState()
		assert.isUndefined(defaultState);
	});

	it('should emit a change event when new route action is dispatched', () => {
		var spy = sinon.spy();
		routeStore.addChangeListener(spy);
		dispatcher.dispatch({type: "route-state-update"});

		var emittedChangeEvents = spy.callCount;
		assert.equal(1, emittedChangeEvents);
	});
});
