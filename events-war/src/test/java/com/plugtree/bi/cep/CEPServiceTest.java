package com.plugtree.bi.cep;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class CEPServiceTest {

	@Test
	public void testStart() throws Exception {
		CEPService service = CEPService.getInstance();
		Assert.assertNotNull(service);
		
		CEPEvent event = new CEPEvent();
		event.setTime(System.currentTimeMillis());
		event.put("gpsAltitude", Float.valueOf(30.f));
		service.insert(event);
		
		Thread.sleep(200);
		
		Map<String, Long> firedRules = service.getFiredRules();
		Assert.assertNotNull(firedRules);
		System.out.println("firedRules = " + firedRules);
		Long values = firedRules.get("test");
		Assert.assertNotNull(values);
		Assert.assertEquals(values, Long.valueOf(1));
	}
}
