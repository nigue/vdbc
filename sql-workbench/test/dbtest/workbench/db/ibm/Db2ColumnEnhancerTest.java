/*
 * Db2ColumnEnhancerTest
 * 
 *  This file is part of SQL Workbench/J, http://www.sql-workbench.net
 * 
 *  Copyright 2002-2011, Thomas Kellerer
 *  No part of this code may be reused without the permission of the author
 * 
 *  To contact the author please send an email to: support@sql-workbench.net
 */
package workbench.db.ibm;

import java.util.List;
import workbench.db.ColumnIdentifier;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import workbench.TestUtil;
import workbench.WbTestCase;
import workbench.db.TableDefinition;
import workbench.db.TableIdentifier;
import workbench.db.WbConnection;
import static org.junit.Assert.*;

/**
 *
 * @author Thomas Kellerer
 */
public class Db2ColumnEnhancerTest
	extends WbTestCase
{

	public Db2ColumnEnhancerTest()
	{
		super("Db2ColumnEnhancerTest");
	}

	@BeforeClass
	public static void setUpClass()
		throws Exception
	{
		Db2TestUtil.initTestCase();
		
		WbConnection con = Db2TestUtil.getDb2Connection();
		if (con == null) return;

		String sql =
			"CREATE TABLE computed_cols ( \n" +
			"  ID INTEGER, \n" +
			"  ID2 INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 12 INCREMENT BY 2 MINVALUE 12), \n " +
      "  ID3 GENERATED ALWAYS AS (ID * 2) \n" +
			");\n" +
		  "commit;\n";
		TestUtil.executeScript(con, sql);
	}

	@AfterClass
	public static void tearDownClass()
		throws Exception
	{
		WbConnection con = Db2TestUtil.getDb2Connection();
		if (con == null) return;

		String schema = Db2TestUtil.getSchemaName();
		String sql =
			"drop table " + schema + ".computed_cols;\n" +
		  "commit;\n";
		TestUtil.executeScript(con, sql);
	}


	@Test
	public void testUpdateColumnDefinition()
		throws Exception
	{
		WbConnection con = Db2TestUtil.getDb2Connection();
		if (con == null) return;

		TableDefinition tbl = con.getMetadata().getTableDefinition(new TableIdentifier(Db2TestUtil.getSchemaName(), "COMPUTED_COLS"));

		assertNotNull(tbl);
		List<ColumnIdentifier> columns = tbl.getColumns();
		assertNotNull(columns);
		assertEquals(3, columns.size());
		assertTrue(columns.get(1).isAutoincrement());
		assertEquals(columns.get(2).getComputedColumnExpression(), "GENERATED ALWAYS AS (ID * 2)");
	}
}