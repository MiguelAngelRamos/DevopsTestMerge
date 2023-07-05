package com.kibernumacademy.devops.controllers;

import com.kibernumacademy.devops.controllers.StudentController;
import com.kibernumacademy.devops.entitys.Student;
import com.kibernumacademy.devops.services.IStudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
// import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.Arrays;
// import java.util.Optional;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
// import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//* Esta es una clase para las pruebas de integración con mockito para el Proyecto de Devops
public class StudentControllerTest {
   // MockMvc es una clase que proporciona una API de alto nivel para realizar pruebas con Spring MVC
    private MockMvc mockMvc;

    // Con la anotación @Mock, Mockito crea un objeto simulado (mock) de la interfaz IStudentService
    @Mock
    private IStudentService studentService;

    // @BeforeEach se ejecuta antes de cada método de prueba
   @BeforeEach
   public void setup() {
    // Abre los mocks que están anotados con @Mock para esta prueba. 
    // Esto significa que se crean instancias simuladas (mocks) para todos los campos anotados con @Mock en esta clase de prueba.
    MockitoAnnotations.openMocks(this);

    // Crea un nuevo ClassLoaderTemplateResolver. Esta es una clase de Thymeleaf que se utiliza para resolver las plantillas 
    // que se cargan desde el classloader, por lo general, desde el directorio de recursos del proyecto.
    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

    // Establece el prefijo que se añadirá a los nombres de las plantillas cuando Thymeleaf intente resolverlas.
    // En este caso, todas las plantillas deben estar en un subdirectorio "templates/" del classpath.
    templateResolver.setPrefix("templates/");

    // Establece el sufijo que se añadirá a los nombres de las plantillas cuando Thymeleaf intente resolverlas.
    // En este caso, todos los nombres de las plantillas deben terminar con ".html".
    templateResolver.setSuffix(".html");

    // Establece el modo de las plantillas que resolverá este resolver. En este caso, las plantillas deben ser HTML.
    templateResolver.setTemplateMode(TemplateMode.HTML);

    // Crea una nueva instancia de SpringTemplateEngine, que es la implementación principal de la interfaz de TemplateEngine en Thymeleaf.
    SpringTemplateEngine templateEngine = new SpringTemplateEngine();

    // Añade el resolver al motor de plantillas. Cuando se pide al motor de plantillas que procese una plantilla,
    // utilizará este resolver para encontrar la plantilla.
    templateEngine.addTemplateResolver(templateResolver);

    // Crea un nuevo ThymeleafViewResolver. Esta es una implementación de ViewResolver de Spring MVC que utiliza el motor de plantillas de Thymeleaf para resolver vistas.
    ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();

    // Establece el motor de plantillas que utilizará este resolver de vistas.
    viewResolver.setTemplateEngine(templateEngine);

    // Crea una nueva instancia del controlador que se va a probar.
    StudentController studentController = new StudentController(studentService);

    // Crea una instancia de MockMvc para esta prueba. MockMvc es una clase que proporciona una API de alto nivel para realizar pruebas con Spring MVC.
    // Aquí, se utiliza MockMvcBuilders.standaloneSetup para configurar MockMvc con el controlador que se va a probar,
    // y se establece el resolver de vistas que debe utilizar.
    mockMvc = MockMvcBuilders.standaloneSetup(studentController)
            .setViewResolvers(viewResolver)
            .build();
}


    @Test
    // El test verifica que la lista de todos los estudiantes se muestra correctamente
    public void shouldListAllStudents() throws Exception {
        // Crea dos estudiantes
        Student student1 = new Student("James", "Gosling", "jgosling@example.com");
        Student student2 = new Student("Richard", "Stallman", "rstallman@example.com");

        // Simula la respuesta del método listAllStudents de studentService
        given(studentService.listAllStudents()).willReturn(Arrays.asList(student1, student2));

        // Realiza una solicitud GET a "/students"
        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())  // Espera que el estado HTTP sea 200 (OK)
                .andExpect(view().name("students"));  // Espera que la vista devuelta sea "students"
    }
}