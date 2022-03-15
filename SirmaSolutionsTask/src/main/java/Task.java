import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;


public class Task {

    private static final int employeeIndex = 0;
    private static final int projectIndex = 1;
    private static final int dateFromIndex = 2;
    private static final int dateToIndex = 3;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");


    public static void main(String[] args) {
        Map<Integer, List<Employee>> workOnProject = new HashMap<>();
        try {
            readCsvAndAddEmployee(new FileReader("projectWork.csv"), workOnProject);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        showPairingEmployees(workOnProject);
    }

    private static void readCsvAndAddEmployee(FileReader fileReader, Map<Integer, List<Employee>> workOnProject) {
        Scanner scanner = new Scanner(fileReader);
        if (scanner.hasNextLine()) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String[] employeeInfo = scanner.nextLine().split(",");
                addEmployee(employeeInfo, workOnProject);
            }
        }
    }

    private static void showPairingEmployees(Map<Integer, List<Employee>> workOnProject) {
        int empl1 = 0;
        int empl2 = 0;
        long days = 0;
        int projectId = 0;
        for (Integer pId : workOnProject.keySet()) {
            for (int i = 0; i < workOnProject.get(pId).size(); i++) {
                Employee employee1 = workOnProject.get(pId).get(i);
                for (int j = 1; j < workOnProject.get(pId).size(); j++) {
                    if (workOnProject.get(pId).size() > 1) {
                        Employee employee2 = workOnProject.get(pId).get(j);
                        long temp = calculateDays(employee1, employee2);
                        if (temp > days) {
                            days = temp;
                            empl1 = employee1.getId();
                            empl2 = employee2.getId();
                            projectId = employee2.getProjectId();
                        }
                    }
                }
            }
        }
        System.out.println(empl1 + ", " + empl2 + ", projectId: " + projectId + ", days: " + days);
    }

    private static long calculateDays(Employee e1, Employee e2) {
        LocalDate start;
        LocalDate end;
        LocalDate empl1From = e1.getDateFrom();
        LocalDate empl1To = e1.getDateTo();
        LocalDate empl2From = e2.getDateFrom();
        LocalDate empl2To = e2.getDateTo();
        if (empl1From.isAfter(empl2From) && empl1From.isBefore(empl2To) && empl1To.isAfter(empl2To)) {
            start = empl1From;
            end = empl2To;
            return ChronoUnit.DAYS.between(start, end);
        }
        if (empl1From.isAfter(empl2From) && empl1From.isBefore(empl2To) && empl1To.isBefore(empl2To)) {
            start = empl1From;
            end = empl1To;
            return ChronoUnit.DAYS.between(start, end);
        }
        if (empl1From.isBefore(empl2From) && empl1To.isBefore(empl2To) && empl1To.isAfter(empl2From)) {
            start = empl2From;
            end = empl1To;
            return ChronoUnit.DAYS.between(start, end);
        }
        if (empl2From.isAfter(empl1From) && empl1From.isBefore(empl2To) && empl2To.isBefore(empl1To)) {
            start = empl2From;
            end = empl2To;
            return ChronoUnit.DAYS.between(start, end);
        }
        if (empl2From.isAfter(empl1From) && empl1From.isBefore(empl2To) && empl2To.isAfter(empl1To)) {
            start = empl2From;
            end = empl1To;
            return ChronoUnit.DAYS.between(start, end);
        }
        if (empl2From.isBefore(empl1From) && empl2To.isAfter(empl1From) && empl2To.isBefore(empl1To)) {
            start = empl1From;
            end = empl2To;
            return ChronoUnit.DAYS.between(start, end);
        }
        if (empl2From.isBefore(empl1From) && empl2To.isAfter(empl1From) && empl2To.isBefore(empl1To)) {
            start = empl1From;
            end = empl2To;
            return ChronoUnit.DAYS.between(start, end);
        }
        if (empl2From.isBefore(empl1From) && empl2To.isAfter(empl1From) && empl2To.isAfter(empl1To)) {
            start = empl1From;
            end = empl1To;
            return ChronoUnit.DAYS.between(start, end);
        }

        return 0;
    }

    private static void addEmployee(String[] employeeInfo, Map<Integer, List<Employee>> workOnProject) {
        int empId = validateInteger(employeeInfo[employeeIndex]);
        int projectId = validateInteger(employeeInfo[projectIndex]);
        LocalDate dateFrom = validateData(employeeInfo[dateFromIndex]);
        LocalDate dateTo = validateData(employeeInfo[dateToIndex]);
        Employee employee = new Employee(empId, projectId, dateFrom, dateTo);

        if (!workOnProject.containsKey(projectId)) {
            workOnProject.put(projectId, new ArrayList<>());
        }
        if (dateFrom.isAfter(dateTo)) {
            throw new DateTimeException("Wrong dateFrom: " + dateFrom + " is after dateTo: " + dateTo);
        }
        workOnProject.get(projectId).add(employee);
    }

    private static LocalDate validateData(String s) {
        if (s.equalsIgnoreCase("null")) {
            return LocalDate.now();
        } else {
            return LocalDate.parse(s, formatter);
        }
    }

    private static int validateInteger(String s) {
        int id = 0;
        if (s != null) {
            try {
                id = Integer.parseInt(s);
                if (id <= 0) {
                    throw new NumberFormatException("Invalid integer !");
                }
            } catch (NumberFormatException nfe) {
                throw new NumberFormatException("Invalid integer " + s);
            }
        }
        return id;
    }
}

