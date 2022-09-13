package com.empresa.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.empresa.entity.Alumno;
import com.empresa.service.AlumnoService;

@RestController
@RequestMapping("/rest/alumno")
@CrossOrigin(origins = "http://localhost:4200")
public class AlumnoController {

	@Autowired
	private AlumnoService alumnoService;

	@GetMapping
	@ResponseBody
	public ResponseEntity<List<Alumno>> listaAlumno(){
		List<Alumno> lista = alumnoService.listaAlumno();
		return ResponseEntity.ok(lista);
	}

	@PostMapping
	@ResponseBody
	public ResponseEntity<?> inserta(@Valid @RequestBody Alumno obj, Errors errors){
		HashMap<String, Object> salida = new HashMap<>();
		List<String> lstMensajes = new ArrayList<String>();
		salida.put("errores", lstMensajes);
		
		List<ObjectError> lstErrors =  errors.getAllErrors();
		for (ObjectError objectError : lstErrors) {
			objectError.getDefaultMessage();
			lstMensajes.add(objectError.getDefaultMessage());
		}
		if (!CollectionUtils.isEmpty(lstMensajes)) {
			return ResponseEntity.ok(salida);
		}
		
		List<Alumno> lstAlumno = alumnoService.listaAlumnoPorDni(obj.getDni());
		if (!CollectionUtils.isEmpty(lstAlumno)) {
			lstMensajes.add("Ya existe un alumno con DNI ==> " + obj.getDni());
			return ResponseEntity.ok(salida);
		}
		lstAlumno = alumnoService.listaAlumnoPorCorreo(obj.getCorreo());
		if (!CollectionUtils.isEmpty(lstAlumno)) {
			lstMensajes.add("Ya existe un alumno con correo ==> " + obj.getCorreo());
			return ResponseEntity.ok(salida);
		}
		Alumno objSalida = alumnoService.insertaActualizaAlumno(obj);
		if (objSalida == null) {
			lstMensajes.add("Error en el registro");
		}else {
			lstMensajes.add("Se registró el alumno ==> " + objSalida.getIdAlumno());
		}
		return ResponseEntity.ok(salida);
	}
	
	
}




