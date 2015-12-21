package fr.si2m.csnt.poc.concurrency;

import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import fr.si2m.csnt.poc.concurrency.management.Si2mThreadMngt;

public class Main {
	public static int i = 0;
	public static int numThread = 5;
	public static long timeToRune = 10000;
	public static void main(String[] args) {

		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

		ObjectName name = null;
		try {
			name = new ObjectName("fr.si2m.concurrency.jmx:type=Si2mThreadMngtMBean");
			Si2mThreadMngt mbean = new Si2mThreadMngt();
			mbs.registerMBean(mbean, name);
			System.out.println("Lancement monitoring ...");
		}catch (Exception lE) {
			// TODO: handle exception
		}

		ExecutorService executorService = Executors.newCachedThreadPool();

		Set<Callable<String>> callables = new HashSet<Callable<String>>();


		for (i = 0; i < numThread; i++) {
			callables.add(new Callable<String>() {
				public int numThread = i;
				public String call() throws Exception {
					Thread.currentThread().setName("BIG_"+this.numThread);
					try {
						long t = System.currentTimeMillis();
						long timeToRun = t+timeToRune;
						while (timeToRun > System.currentTimeMillis()) {
							if (Thread.currentThread().isInterrupted()) {
								InterruptedException ie = new InterruptedException();
								throw ie;
							} else {
								System.out.print("BIG "+this.numThread+" - ");
							}
							//						Thread.sleep(4000);
						}
					} catch (Exception e) {
						System.out.println(Thread.currentThread().getName() + " - " + e.getMessage());
						throw e;
					}
					return "BIG "+this.numThread+" termine";
				}
			});
		}

		//		callables.add(new Callable<String>() {
		//			public String call() throws Exception {
		//				Thread.currentThread().setName("T1");
		//				try {
		//					while (true) {
		//						if (Thread.currentThread().isInterrupted()) {
		//							InterruptedException ie = new InterruptedException();
		//							throw ie;
		//						} else {
		//							System.out.print("Task 1 - ");
		//						}
		//						//						Thread.sleep(4000);
		//					}
		//				} catch (Exception e) {
		//					System.out.println(Thread.currentThread().getName() + " - " + e.getMessage());
		//					throw e;
		//				}
		//				//				return "Task 1";
		//			}
		//		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				Thread.currentThread().setName("T2");
				for (int i = 0; i < 1000; i++) {
					//				while(true) {
					Thread.sleep(500);
					System.out.println("Task 2");
				}
				return "Task 2";
			}
		});
		callables.add(new Callable<String>() {
			public String call() throws Exception {
				return "Task 3";
			}
		});

		List<Future<String>> futures = null;
		//		try {
		//			futures = executorService.invokeAll(callables, 2000, TimeUnit.MILLISECONDS);
		//			for (Future<String> future : futures) {
		//				boolean isThreadCancelled = false;
		//				if (isThreadCancelled = future.isCancelled()) {
		//					System.out.println("un future "+future.toString()+" a été cancellé");
		//				} else {
		//					try {
		//						System.out.println("future.get = " + future.get());
		//					} catch (InterruptedException e) {
		//						e.printStackTrace();
		//					} catch (ExecutionException e) {
		//						e.printStackTrace();
		//					}
		//				}
		//			}
		//		} catch (InterruptedException e) {
		//			e.printStackTrace();
		//		}
		//
		//		executorService.shutdown();
		try {
			futures = executorService.invokeAll(callables, 5000, TimeUnit.MILLISECONDS);
			for (Future<String> future : futures) {
				boolean isThreadCancelled = false;
				if (isThreadCancelled = future.isCancelled()) {
					System.out.println("un future "+future.toString()+" a été cancellé");
				} else {
					System.out.println("un future arrive à terme");
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			if (null != futures) {
				for (final Future<String> future : futures) {
					if (!future.isCancelled()) {
						future.cancel(Boolean.TRUE);
						System.out.println("T1");
					}
				}
			}
			executorService.shutdown();
			executorService.shutdownNow();
		}
	}
}
