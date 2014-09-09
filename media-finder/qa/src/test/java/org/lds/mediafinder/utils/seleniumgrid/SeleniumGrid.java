package org.lds.mediafinder.utils.seleniumgrid;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.lds.stack.utils.SshUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * 
 * The Grid is a vast part of the [Selenium] system, programmed by Kevin Flynn. Often referred to by Flynn as his "digital frontier", 
 * the Grid was made to provide an experimental platform where all forms of research could be carried out at unparalleled speeds. 
 * Perceived time on the Grid is measured in cycles and run at a pace far greater than time perceived in the real world, 
 * thus allowing anyone immersed in the computer environment to perform the same functions in a fraction of the time it would take them otherwise. 
 * Much of the Grid consists of a flat, dark platform with glowing blue, cyan, or white, ribbons of light covering it in a vast latticework. 
 * Within the Grid itself lies [Selenium] City, a metropolis modeled on a real world city, which plays host to a diverse range of programs. 
 * The programs in turn carry out all the day-to-day running of the system. On the periphery of the Grid lies the Outlands, 
 * an inhospitable region where most programs never venture.
 * 
 * @see http://labslcl084:4444/grid/console
 * @see http://code.google.com/p/selenium/wiki/Grid2
 * @see http://tron.wikia.com/wiki/Grid
 * 
 * @author SilverColt <mgalloway@ldschurch.org>
 *
 */
public class SeleniumGrid {

	protected String gridHub = "http://grid-hub.ldschurch.org"; //"labslcl084"; //10.118.195.115
	//protected Integer gridHubPort = 4444;
	public WebDriver driver = null;
	
	public SeleniumGrid(SeleniumGrid.Browser browser, String version, Platform platform) throws MalformedURLException {
		
		URL hub = new URL(gridHub + "/wd/hub");
		DesiredCapabilities capabilities = null;
		switch(browser) {
		
			case FIREFOX : 
				capabilities = getFirefoxCapabilities(browser.get(), platform, version);
				break;
			case CHROME : 
				capabilities = getChromeCapabilities(browser.get(), platform, version);
				break;
			case IEXPLORER : 
				capabilities = getIeCapabilities(browser.get(), platform, version);
				break;
			default : 
				capabilities = getChromeCapabilities(browser.get(), platform, version);
		
		}
		
		setNode(hub, capabilities); 
		
	}
	
	private void setNode(URL hub, DesiredCapabilities capabilities) throws MalformedURLException {
		
		driver = new RemoteWebDriver(hub, capabilities);
		
	}
	
	private DesiredCapabilities getIeCapabilities(String browser, Platform platform, String version) {
		
		DesiredCapabilities capabilities = null;	
		capabilities = DesiredCapabilities.internetExplorer();
		capabilities.setPlatform(platform);
		capabilities.setBrowserName(browser); 
		capabilities.setVersion(version);		
		return capabilities;
	}

	private DesiredCapabilities getChromeCapabilities(String browser, Platform platform, String version) {
		
		DesiredCapabilities capabilities = null;
		capabilities = DesiredCapabilities.chrome();
		capabilities.setBrowserName(browser); 
		capabilities.setPlatform(platform);
		capabilities.setVersion(version);
		capabilities.setJavascriptEnabled(true);
		return capabilities;
	}

	private DesiredCapabilities getFirefoxCapabilities(String browser, Platform platform, String version) {
		
		DesiredCapabilities capabilities = null;
		FirefoxProfile profile = new FirefoxProfile();
		//profile.addExtension(new File("./src/test/resources/firebug.xpi"));
		profile.setAcceptUntrustedCertificates(true);		
		capabilities = DesiredCapabilities.firefox();
		capabilities.setCapability("firefox_profile", profile);
		capabilities.setBrowserName(browser);
		capabilities.setPlatform(platform);
		capabilities.setVersion(version);
		capabilities.setJavascriptEnabled(true);
		return capabilities;
		
	}
	
	public enum Browser {
		
		FIREFOX("firefox"),
		CHROME("chrome"),
		IEXPLORER("internet explorer");
		
		private String browser;
		
		private Browser(String browser) {
			this.browser = browser;
		}
		
		public String get() {
			return browser;
		}
		  
	}
	
	/**
	 * Captures a screen-shot at the time called and copies it to the following webserver
	 * <pre>http://labslcl0847/screenshots/</pre>
	 * Screen-shots are saved using the following format
	 * <pre>screenshot9030797293887415906.png</pre>
	 * @return String http path to screen-shot
	 * @throws Exception 
	 */
	public String captureScreen() {
		 
		String path;
		try {
			WebDriver augmentedDriver = new Augmenter().augment(driver);
			File source = ((TakesScreenshot)augmentedDriver).getScreenshotAs(OutputType.FILE);
			String serverPath = "/var/www/screenshots/" + source.getName();
			Session sshSession = getSshSession();
			SshUtils.sendFile(sshSession, source.getName(), "scp -p -t " + serverPath, source);
			path = "http://labslcl0847/screenshots/" + source.getName();
		}
		catch(Exception e) {
			path = "Failed to capture screenshot:";
			e.printStackTrace();
		}
		return path;
		
	}
	
	private Session getSshSession() throws Exception, JSchException {
		
		String host = "labslcl0847";
		String user = "tron";
		String pass = "ewokman";
		//String keyFile = "/home/tron/.ssh/id_rsa";
		// String passphrase = "";
		return SshUtils.connect(host, user, pass, null, null, null);
		
	}
	
}
