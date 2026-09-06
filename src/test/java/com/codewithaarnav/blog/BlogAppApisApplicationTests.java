package com.codewithaarnav.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.codewithaarnav.blog.repositories.UserRepo;

@SpringBootTest
class BlogAppApisApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Autowired
	private UserRepo userRepo;
	
	@Test
	public void repoTest() {
		String className = this.userRepo.getClass().getName();
		String packName = this.userRepo.getClass().getPackageName();
		System.out.println(className);
		System.out.println(packName);
	}

}
