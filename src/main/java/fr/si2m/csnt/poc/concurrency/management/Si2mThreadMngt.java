package fr.si2m.csnt.poc.concurrency.management;

public class Si2mThreadMngt implements Si2mThreadMngtMBean {

	public String interuptThread(final String pGroupName, final String pThreadName) {
		ThreadGroup[] lThreadGroupList = null;
		Thread[] lThreadList = null;
		ThreadGroup lThreadGroup = null;
		Thread lThread = null;


		lThreadGroup = Thread.currentThread().getThreadGroup().getParent();
		lThreadGroupList = new ThreadGroup[lThreadGroup.activeGroupCount()];
		int grpMax = lThreadGroup.enumerate(lThreadGroupList);
		for (int i = 0; (i < lThreadGroupList.length) && (i < grpMax); i++) {
			if (pGroupName.equals(lThreadGroupList[i].getName())) {
				lThreadList = new Thread[lThreadGroupList[i].activeCount()];
				int thMax = lThreadGroupList[i].enumerate(lThreadList);
				for (int j = 0; (j < lThreadList.length) && (j < thMax); j++) {
					if (pThreadName.equals(lThreadList[j].getName())) {
						lThreadList[j].interrupt();
						return pThreadName+" interrompu";
					}
				}
				return "Thread '"+pThreadName+"' introuvable";
			}
		}
		return "Groupe '"+pGroupName+"' introuvable";
	}



	//		lThreadGroup = Thread.currentThread().getThreadGroup().getParent();
	//		lThreadGroupList = new ThreadGroup[10];
	//		int grpMax = lThreadGroup.enumerate(lThreadGroupList);
	//		for (int i = 0; (i < lThreadGroupList.length) && (i < grpMax); i++) {
	//			if (pGroupName.equals(lThreadGroupList[i].getName())) {
	//				lThreadList = new Thread[10];
	//				int thMax = lThreadGroupList[i].enumerate(lThreadList);
	//				for (int j = 0; (j < lThreadList.length) && (j < thMax); j++) {
	//					if (pThreadName.equals(lThreadList[j].getName())) {
	//						lThreadList[j].interrupt();
	//						return pThreadName+" interrompu";
	//					}
	//				}
	//				return "Thread '"+pThreadName+"' introuvable";
	//			}
	//		}
	//		return "Groupe '"+pGroupName+"' introuvable";
	//	}

}
