package org.sagebionetworks.repo.manager;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-context.xml" })
public class NodeManagerImplTest {
	
//	@Autowired
	public NodeManager nodeManager;
	
	@Test
	public void before(){
//		assertNotNull(nodeManager);
	}

}
