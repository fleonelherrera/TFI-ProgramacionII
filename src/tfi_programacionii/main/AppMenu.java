package tfi_programacionii.main;

import tfi_programacionii.service.IPacienteService;
import tfi_programacionii.service.IHistoriaClinicaService;
import tfi_programacionii.service.PacienteService;
import tfi_programacionii.service.HistoriaClinicaService;
import tfi_programacionii.dao.PacienteDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import tfi_programacionii.enums.GrupoSanguineo;
import tfi_programacionii.models.HistoriaClinica;
import tfi_programacionii.models.Paciente;

public class AppMenu {
    private IPacienteService pacienteService;
    private IHistoriaClinicaService historiaClinicaService;
    private Scanner scanner;
    
    public AppMenu() {
        this.historiaClinicaService = new HistoriaClinicaService();
        this.pacienteService = new PacienteService();
        this.scanner = new Scanner(System.in);
    }
    
    public void ejecutar() {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = getInt("Ingrese una opción");
            
            try {
                switch (opcion) {
                    case 1: crearPaciente(); break;
                    case 2: leerPacientePorId(); break;
                    case 3: listarPacientes(); break;
                    case 4: actualizarPaciente(); break;
                    case 5: eliminarPaciente(); break;
                    case 6: buscarPacientePorDni(); break;
                    case 0: System.out.println("Saliendo...");; break;
                    default: System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.err.println("Error en la operacion: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Error inesperado. " + e.getMessage());
            }
            System.out.println("\n----------------------------------------------\n");
        } while (opcion != 0);
        
        scanner.close();
    }
    
    private String getString(String prompt) {
        System.out.println(prompt + ": ");
        return scanner.nextLine();
    }
    
    private int getInt(String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                int valor = Integer.parseInt(scanner.nextLine());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida.");
            }
        }
    }
    
    private LocalDate getDate(String prompt) {
        while (true) {
            System.out.print(prompt + " (YYYY-MM-DD): ");
            try {
                return LocalDate.parse(scanner.nextLine());
            } catch (DateTimeParseException e) {
                System.out.println("Formato de fecha inválido.");
            }
        }
    }
    
    private void mostrarMenuPrincipal() {
        System.out.println("--- Sistema de gestión de Pacientes ---");
        System.out.println("1. Ingresar nuevo Paciente");
        System.out.println("2. Buscar Paciente por ID");
        System.out.println("3. Listar todos los Pacientes");
        System.out.println("4. Actualizar datos de Paciente");
        System.out.println("5. Eliminar Paciente");
        System.out.println("6. Buscar Paciente por DNI");
        System.out.println("0. Salir");
    }
    
    private void crearPaciente() throws SQLException {
        System.out.println("--- Crear nuevo Paciente ---");
        String nombre = getString("Nombre").toUpperCase();
        String apellido = getString("Apellido").toUpperCase();
        String dni = getString("DNI").toUpperCase();
        LocalDate fechaNacimiento = getDate("Fecha de Nacimiento");
        
        System.out.println("\n--- Datos de Historia Clinica ---");
        String nroHistoria = getString("Número de Historia Clínica").toUpperCase();
        
        GrupoSanguineo grupoSanguineo = null;
        while (grupoSanguineo == null) {
            String grupoSanguineoStr = getString("Grupo sanguineo (ej: A+, O-)").toUpperCase();
            try {
                grupoSanguineo = GrupoSanguineo.fromAbreviatura(grupoSanguineoStr);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        
        String antecedentes = getString("Antecedentes".toUpperCase());
        String medicacionActual = getString("Medicación actual".toUpperCase());
        String observaciones = getString("Observaciones".toUpperCase());
        
        Paciente nuevoPaciente = new Paciente(nombre, apellido, dni, fechaNacimiento, nroHistoria, grupoSanguineo, antecedentes, medicacionActual, observaciones);
        
        pacienteService.insertar(nuevoPaciente);
        System.out.println("Paciente creado exitosamente.");
    }
    
    private void leerPacientePorId() throws SQLException {
        System.out.println("--- Buscar Paciente ---");
        int id = getInt("Ingrese el ID del Paciente");
        Paciente paciente = pacienteService.getById(id);
        
        if (paciente != null) {
            System.out.println("\n--- Datos del Paciente ---");
            System.out.println(paciente.toString());
            System.out.println("\n--- Historia Clínica ---");
            System.out.println(paciente.getHistoriaClinica().toString());
        } else {
            System.out.println("El paciente con ID " + id + " no se encuentra.");
        }
    }
    
    private void listarPacientes() throws SQLException {
        System.out.println("--- Listado de Pacientes ---");
        List<Paciente> pacientes = pacienteService.getAll();
        
        if (pacientes.isEmpty()) {
            System.out.println("No se encuentran pacientes.");
        } else {
            for (Paciente p : pacientes) {
                System.out.println(p.toString());
            }
        }
    }
    
    private void actualizarPaciente() throws SQLException {
        System.out.println("--- Actualizar Datos de Paciente ---");
        int id = getInt("Ingrese el ID del Paciente a actualizar");
        Paciente pacienteAActualizar = pacienteService.getById(id);
        
        if (pacienteAActualizar == null || pacienteAActualizar.isEliminado()) {
            System.out.println("El paciente con ID " + id + " no se encuentra.");
            return;
        }
        
        System.out.println("\n--- Datos del Paciente ---");
        
        System.out.println("Nombre actual: " + pacienteAActualizar.getNombre());
        pacienteAActualizar.setNombre(getString("Nuevo nombre").toUpperCase());
        
        System.out.println("Apellido actual: " + pacienteAActualizar.getApellido());
        pacienteAActualizar.setApellido(getString("Nuevo apellido").toUpperCase());
        
        System.out.println("DNI actual: " + pacienteAActualizar.getDni());
        pacienteAActualizar.setDni(getString("Nuevo DNI").toUpperCase());
        
        System.out.println("Fecha Nacimiento actual: " + pacienteAActualizar.getFechaNacimiento());
        pacienteAActualizar.setFechaNacimiento(getDate("Nueva Fecha de Nacimiento"));
        
        // ACTUALIZO DATOS DE LA HISTORIA CLINICA
        HistoriaClinica hcDelPaciente = pacienteAActualizar.getHistoriaClinica();
        System.out.println("Nro. actual: " + hcDelPaciente.getNroHistoria());
        hcDelPaciente.setNroHistoria(getString("Nuevo Nro de Historia").toUpperCase());
        
        // EN ESTE CASO, CICLAMOS HASTA QUE EL USUARIO INGRESE UN GRUPO SANGUINEO VALIDO.
        System.out.println("Grupo Sanguineo actual: " + hcDelPaciente.getGrupoSanguineo().getAbreviatura());
        GrupoSanguineo grupoSanguineo = null;
        while (grupoSanguineo == null) {
            String grupoSanguineoStr = getString("Nuevo Grupo sanguineo (ej: A+, O-)").toUpperCase();
            try {
                grupoSanguineo = GrupoSanguineo.fromAbreviatura(grupoSanguineoStr);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
        hcDelPaciente.setGrupoSanguineo(grupoSanguineo);
        
        System.out.println("Antecedentes actuales: " + hcDelPaciente.getAntecedentes());
        hcDelPaciente.setAntecedentes(getString("Nuevos Antecedentes").toUpperCase());
        
        System.out.println("Medicación actual: " + hcDelPaciente.getMedicacionActual());
        hcDelPaciente.setMedicacionActual(getString("Nueva Medicación").toUpperCase());
        
        System.out.println("Observaciones actuales: " + hcDelPaciente.getObservaciones());
        hcDelPaciente.setObservaciones(getString("Nuevas Observaciones").toUpperCase());

        pacienteService.actualizar(pacienteAActualizar);
        System.out.println("Paciente actualizado correctamente.");
    }
    
    private void eliminarPaciente() throws SQLException {
        System.out.println("--- Eliminar Paciente ---");
        int id = getInt("Ingrese el ID del Paciente a eliminar");
        
        pacienteService.eliminar(id);
        System.out.println("El Paciente con ID " + id + " ha sido eliminado.");
    }
    
    private void buscarPacientePorDni() throws SQLException {
        System.out.println("--- Buscar Paciente por DNI ---");
        String dni = getString("Ingrese DNI del paciente").toUpperCase();
        Paciente paciente = pacienteService.buscarPorDni(dni);
        
        if (paciente != null && !paciente.isEliminado()) {
            System.out.println("\n--- Datos del Paciente ---");
            System.out.println(paciente.toString());
            System.out.println("\n--- Historia Clínica ---");
            System.out.println(paciente.getHistoriaClinica().toString());
        } else {
            System.out.println("Paciente con DNI " + dni + " no encontrado.");
        }
    }
}
