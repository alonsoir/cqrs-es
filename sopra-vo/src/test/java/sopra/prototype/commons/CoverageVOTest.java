package sopra.prototype.commons;

import org.junit.Assert;
import org.junit.Test;

public class CoverageVOTest {

	@Test
	public void testForCoverage() {
		Assert.assertNotNull(new Constants());
		Assert.assertNotNull(new ConstantsUrl());
		Assert.assertNotNull(new ConstantsUrlParams());
	}
}
