package es.sopra.prototype.commons;

import org.junit.Assert;
import org.junit.Test;

import es.sopra.prototype.commons.Constants;
import es.sopra.prototype.commons.ConstantsUrl;
import es.sopra.prototype.commons.ConstantsUrlParams;

public class CoverageVOTest {

	@Test
	public void testForCoverage() {
		Assert.assertNotNull(new Constants());
		Assert.assertNotNull(new ConstantsUrl());
		Assert.assertNotNull(new ConstantsUrlParams());
	}
}
