package Test;

import org.junit.Test;

import application.Encoder;
import junit.framework.TestCase;

public class TestEncoder extends TestCase {
	
	private String justSpaces = "        ";
	private String justTabs = "t\t\t\t\t\t";
	private String randomString1 = "adminadminAdmin";
	private String randomString2 = "userUser";
    
	@Test
	public void testEncodeDecode() {
		String result;
		for(int i = 0; i < 100; i++) {
			result = Encoder.decode(Encoder.encode(justSpaces));
			assertEquals(justSpaces, result);
			
			result = Encoder.decode(Encoder.encode(justTabs));
			assertEquals(justTabs, result);
			
			result = Encoder.decode(Encoder.encode(randomString1));
			assertEquals(randomString1, result);
			
			result = Encoder.decode(Encoder.encode(randomString2));
			assertEquals(randomString2, result);
		}		
	}	
}
