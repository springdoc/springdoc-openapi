package v31.app248

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

BASE_PACKAGE = 'test.org.springdoc.api.v31.app248'

def parse(String qualifiedClassName) {
	parts = qualifiedClassName.split('\\.')
	packages = parts[0..-2]
	className = parts[-1]
	return [className, packages]
}

def package_name(List<String> packages) {
	return String.join('.', [BASE_PACKAGE] + packages)
}

def directory(List<String> packages) {
	packages = BASE_PACKAGE.split('\\.') + packages
	return Paths.get(project.build.directory, "generated-test-sources", *packages)
}

Path filePath(String className, List<String> packages) {
	return directory(packages).resolve(className + ".java")
}

def generateDTO(String className, properties) {
	(className, packages) = parse(className)
	def package_ = package_name(packages)
	def path = filePath(className, packages)
	Files.createDirectories(path.parent)
	Files.newBufferedWriter(path).withWriter { w ->
		w << "package ${package_};\n\n"
		w << "public class ${className} {\n\n"
		properties.each { type, name ->
			w << "\tprivate $type $name;\n\n"
		}
		w << "\tpublic $className() {}\n\n"
		properties.each { type, name ->
			def capitalized = name.capitalize()
			w << "\tpublic $type get$capitalized() {\n"
			w << "\t\treturn this.$name;\n"
			w << "\t}\n\n"
			w << "\tpublic void set$capitalized($type $name) {\n"
			w << "\t\tthis.$name = $name;\n"
			w << "\t}\n\n"
		}
		w << "}\n\n"
	}
}

def generateController(String className, String requestPath) {
	def imports = [
			'java.util.List',
			'org.springframework.http.ResponseEntity',
			'org.springframework.web.bind.annotation.GetMapping',
			'org.springframework.web.bind.annotation.PostMapping',
			'org.springframework.web.bind.annotation.RequestMapping',
			'org.springframework.web.bind.annotation.RestController',
			'test.org.springdoc.api.v31.app248.dto.PersonDTO'
	]

	(className, packages) = parse(className)
	def package_ = package_name(packages)
	def path = filePath(className, packages)
	Files.createDirectories(path.parent)
	Files.newBufferedWriter(path).withWriter { w ->
		w << "package ${package_};\n\n"
		imports.each { imp -> w << "import $imp;\n\n" }
		w << "\n@RestController\n"
		w << "@RequestMapping(\"$requestPath\")\n"
		w << "public class ${className} {\n\n"
		w << "\t@PostMapping\n"
		w << "\tpublic ResponseEntity<PersonDTO> createPerson(PersonDTO createDto) {\n"
		w << "\t\treturn null;\n"
		w << "\t}\n\n"
		w << "\t@GetMapping\n"
		w << "\tpublic ResponseEntity<List<PersonDTO>> listPerson() {\n"
		w << "\t\treturn null;\n"
		w << "\t}\n\n"
		w << "\t@GetMapping(\"{id}\")\n"
		w << "\tpublic ResponseEntity<PersonDTO> getPerson(Long id) {\n"
		w << "\t\treturn null;\n"
		w << "\t}\n\n"
		w << "}\n"
	}
}

def main() {
	generateDTO('dto.PersonDTO', [
			['Long', 'id'],
			['String', 'name'],
			['Address', 'address'],
			['PersonDetails1', 'details']
	])

	def numControllers = 200
	def numDtos = 512
	(1..numDtos).each { i ->
		def className = "dto.PersonDetails$i"
		def props = [['String', 'value']]
		if (2 * i <= numDtos) {
			props << ["PersonDetails${2 * i}", 'details1']
		}
		if (2 * i + 1 <= numDtos) {
			props << ["PersonDetails${2 * i + 1}", 'details2']
		}
		generateDTO(className, props)
	}

	(1..numControllers).each { i ->
		generateController("controller.Hello${i}Controller", "test${i}")
	}

	Paths.get(project.build.directory, "generated-test-sources")
			.toFile()
			.eachFileRecurse {file ->
				println(file)
			}
}

main()