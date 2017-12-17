package jfeir.audio.capture

import java.io.IOException;
import java.nio.charset.Charset
import java.nio.file.Path
import java.text.SimpleDateFormat
import java.nio.file.Files

class Capture{


//TODO; getResourceAsStream

	private static final int SECONDS=0
	private static final int MINUTES=1
	private static final int HOURS=2
	private static final int DAYS=3

	private String genre
	private int duration
	private int unit

	private programPath = null
	private baseURL = null
	private key = null
	private outputPath = null
	private userAgent = null
	private List<String> captureList = null


	// TODO - create duration array for lookups
	// TODO - find prop files on classpath

	public List<String> getCaptureList() {
		return captureList;
	}

	public void setCaptureList(List<String> captureList) {
		this.captureList = captureList;
	}

	Capture() {

//		loadPropertiesFromClassPath();

		loadProperties();
		loadCaptureList();


	}

//	Capture(String genre, int duration, int unit){
//		this.genre = genre
//		this.duration = duration
//		this.unit = unit
//	}


	public String getGenre() {
		return genre;
	}


	public void setGenre(String genre) {
		this.genre = genre;
	}


	public int getDuration() {
		return duration;
	}


	public void setDuration(int duration) {
		this.duration = duration;
	}


	public int getUnit() {
		return unit;
	}


	public void setUnit(int unit) {
		this.unit = unit;
	}

	private void loadProperties() {

		//		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		//		InputStream is = classLoader.getResourceAsStream("capture.properties");

//		InputStream is = new FileInputStream("../../../../src/main/resources/capture.properties");
		InputStream is = new FileInputStream("C:/Users/jfeir/workspace2/capture/src/main/resources/capture.properties");
		if (is == null) {
			system.err.println("capture:loadProperties:ERROR:prop file not found.")
			return;
		}


		Properties prop = new Properties();
		prop.load(is);

		key = prop.get("key");
		programPath = prop.get("programPath");
		baseURL = prop.get("baseURL");
		outputPath = prop.get("outputPath");
		userAgent = prop.get("userAgent");

	}

	// not working
	private void loadPropertiesFromClassPath() {
		Properties prop = new Properties();

		InputStream is = getClass().getClassLoader().getResourceAsStream("capture.properties");

		if (is != null) {
			prop.load(is);
		}
		else {
			system.err.println("capture:loadPropertiesFromClassPath:ERROR:prop file not found.")
		}

	}

	private void loadCaptureList() {
		Path filePath = new File("C:/Users/jfeir/workspace2/capture/src/main/resources/genre.properties").toPath();
		Charset charset = Charset.defaultCharset();
		captureList = Files.readAllLines(filePath, charset);


	}

	def runCommand(command) {
		def process = command.execute()
		process.waitForProcessOutput(System.out, System.err)
		return process
	}

	private String getUnitString (int unit) {
		String unitString
		if (unit==SECONDS) {
			unitString = "seconds"
		}
		else {
			if (unit==MINUTES) {
				unitString = "minutes"

			}
			else {
				if (unit==HOURS) {
					unitString = "hours"

				}
				else {
					if (unit==DAYS) {
						unitString = "days"

					}
					else {
						println "Invalid units: ${unit}"
						System.exit(1)
					}
				}
			}

		}
		return unitString


	}

	private int getSeconds (int unit, int duration) {
		def int seconds = 0
		if (unit==SECONDS) {
			seconds = duration
		}
		else {
			if (unit==MINUTES) {
				seconds = duration*60

			}
			else {
				if (unit==HOURS) {
					seconds = duration*3600

				}
				else {
					if (unit==DAYS) {
						seconds = duration*3600*24

					}
					else {
						println "Invalid units: ${unit}"
						System.exit(1)
					}
				}
			}
		}
		return seconds

	}

	private int getUnitKey(String unitText) {
		int iUnit
		if (unitText.equals("SECONDS")) {
			iUnit = 0;
		}
		else {
			if (unitText.equals("MINUTES")) {
				iUnit = 1;

			}
			else {
				if (unitText.equals("HOURS")) {
					iUnit = 2;

				}
				else {
					if (unitText.equals("DAYS")) {
						iUnit = 3;

					}
					else {
						println "Invalid units: ${unitText}"
						System.exit(1)
					}
				}
			}
		}
		return iUnit

	}


	void run() {

		def int durationSeconds

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String startTime = sdf.format(new Date());

		durationSeconds = getSeconds(unit, duration)

		def cmd = "${programPath} ${baseURL}/${genre}?${key} -d ${outputPath} -u ${userAgent} -l ${durationSeconds} --quiet"
		println "Start Capturing: ${genre} for ${duration} " + getUnitString(unit) + "(s) at " + startTime
		runCommand(cmd)
		sdf = new SimpleDateFormat("HH:mm:ss");
		String endTime = sdf.format(new Date());

		println "End Capturing: ${genre} at " + endTime

	}

	static main(arguments) {

		def c = new Capture();

		for (String capture : c.getCaptureList()) {
			if (capture.startsWith("#") || capture.startsWith("//") || capture.trim().length()==0) {
				continue;
			}
			String[] elements = capture.split(",");
			if (elements.length < 3) {
				System.err.println("capture:main:ERROR:could not parse capture list line:"+capture);
				return;
			}
			c.setGenre(elements[0]);
			c.setDuration(Integer.parseInt(elements[1]));
			c.setUnit(c.getUnitKey(elements[2]));
			c.run();

		}


	}



}
