package utils;

import java.util.concurrent.Semaphore;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import play.test.Helpers;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;

/**
 *
 * @author eriklark
 */
class PlayIntegrationRule implements TestRule {

	@Override
	public Statement apply(final Statement base, final Description description) {
		return new PlayFakeApplicationStatement(base);
	}

	private class PlayFakeApplicationStatement extends Statement {

		private final Statement base;

		public PlayFakeApplicationStatement(Statement base) {
			this.base = base;
		}

		@Override
		public void evaluate() throws Throwable {
			final Holder<Throwable> throwed = new Holder<>();
			final Semaphore sem = new Semaphore(0);

			running(fakeApplication(Helpers.inMemoryDatabase()), new Runnable() {

				@Override
				public void run() {
					try {
						base.evaluate();
					} catch (Throwable ex) {
						throwed.t = ex;
					} finally {
						sem.release();
					}
				}
			});

			sem.acquireUninterruptibly();
			if (throwed.t != null) {
				throw throwed.t;
			}
		}

		private class Holder<T> {
			T t;
		}
	}
}
