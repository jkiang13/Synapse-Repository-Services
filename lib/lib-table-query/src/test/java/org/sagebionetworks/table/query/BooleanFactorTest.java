package org.sagebionetworks.table.query;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.sagebionetworks.table.query.model.BooleanFactor;
import org.sagebionetworks.table.query.model.BooleanTest;
import org.sagebionetworks.table.query.util.SqlElementUntils;

public class BooleanFactorTest {
	
	@Test
	public void testBooleanFactorToSQLMissingNot() throws ParseException{
		BooleanTest booleanTest = SqlElementUntils.createBooleanTest("foo>1");
		BooleanFactor element = new  BooleanFactor(null , booleanTest);
		assertEquals("foo > 1", element.toString());
	}
	
	@Test
	public void testBooleanFactorToSQLNot() throws ParseException{
		BooleanTest booleanTest = SqlElementUntils.createBooleanTest("foo>1");
		BooleanFactor element = new  BooleanFactor(Boolean.TRUE , booleanTest);
		assertEquals("NOT foo > 1", element.toString());
	}
}
