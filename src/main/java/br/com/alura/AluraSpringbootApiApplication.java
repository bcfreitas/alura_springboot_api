package br.com.alura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
//habilita que um objeto Pageable seja passado diretamente no metodo de um endpoint
//possibilitando urls com ?page=2&size=10&sort=id,asc
@EnableSpringDataWebSupport
//habilita uso de cache (dependencia spring-boot-starter-cache)
@EnableCaching
public class AluraSpringbootApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AluraSpringbootApiApplication.class, args);
	}

}
