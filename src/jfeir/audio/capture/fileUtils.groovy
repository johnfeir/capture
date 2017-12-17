package jfeir.audio.capture

class FileUtils{


	FileUtils(){
		//		this.unit = unit
	}

	FilenameFilter mp3Filter = new FilenameFilter() {
		public boolean accept(File file, String name) {
			if (name.endsWith(".mp3")) {
				// filters files whose extension is .mp3
				return true;
			} else {
				return false;
			}
		}
	};
	
	List<File> buildFileList(String base, String pattern) {
		//		def files = new FileNameFinder().getFileNames(basePath, pattern)

		File basePath = new File(base);
//		def list=[]
//		def basePath = new File(base)

		File[] files = basePath.listFiles(mp3Filter);
		
		
//		new File(basePath).eachFileMatch(~/.*.mp3/) { file ->
//			println file.getName()
//		 }
		
//			basePath.eachFile {
//			list.add(it.name)
//		}
			
		return files

	}



	static main(arguments) {

		def fu = new FileUtils()
		List <File> filesToCheck = fu.buildFileList("C:/audio/stream-rip/Vocal Chillout - Digitally Imported Premium", "*.mp3")
		List <File> existingFiles = fu.buildFileList("Z:/audio/2014/Vocal Chillout - Digitally Imported Premium", "*.mp3")
		
		for (File file : filesToCheck) {
			System.out.println(file.name);
		}

	}


}




