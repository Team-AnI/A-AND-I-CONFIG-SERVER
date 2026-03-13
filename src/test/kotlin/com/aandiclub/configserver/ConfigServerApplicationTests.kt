package com.aandiclub.configserver

import org.junit.jupiter.api.Test
import org.hamcrest.Matchers.containsString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@AutoConfigureMockMvc
@SpringBootTest
class ConfigServerApplicationTests(
	@Autowired private val mockMvc: MockMvc,
) {

	@Test
	fun contextLoads() {
	}

	@Test
	fun `health endpoint is publicly reachable`() {
		mockMvc.perform(get("/actuator/health"))
			.andExpect(status().isOk)
			.andExpect(jsonPath("$.status").value("UP"))
	}

	@Test
	fun `config lookup requires authentication`() {
		mockMvc.perform(get("/auth/default"))
			.andExpect(status().isUnauthorized)
	}

	@Test
	fun `config lookup works with authentication`() {
		mockMvc.perform(
			get("/auth/default")
				.with(httpBasic("config-user", "change-this-before-deploy")),
		)
			.andExpect(status().isOk)
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(content().string(containsString("\"name\":\"auth\"")))
			.andExpect(content().string(containsString("\"jwt.issuer\":\"https://auth.local.example.test\"")))
			.andExpect(content().string(containsString("\"common.message\":\"hello from local config server sample\"")))
	}
}
