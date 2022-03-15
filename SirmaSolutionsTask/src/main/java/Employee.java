import java.time.LocalDate;

public class Employee {
    private int id;
    private int projectId;
    private LocalDate dateFrom;
    private LocalDate dateTo;


    public Employee(int id, int projectId, LocalDate dateFrom, LocalDate dateTo) {
        this.id = id;
        this.projectId = projectId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public int getProjectId() {
        return projectId;
    }
}
