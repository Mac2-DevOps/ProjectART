# USE CASE: 3 Produce a Report on the Salary of Employees in a Manager's Department

## CHARACTERISTIC INFORMATION

### Goal in Context

As a *department manager* I want *to produce a report on the salary of employees in my department* so that *I can support departmental planning and financial reporting.*

### Scope

Company.

### Level

Primary task.

### Preconditions

The manager is authenticated and associated with a department. The database contains current employee salary data and department assignments.

### Success End Condition

A report containing the current salaries of employees in the manager's department is available to the manager.

### Failed End Condition

No report is produced.

### Primary Actor

Department Manager.

### Trigger

The department manager requests salary information for their department.

## MAIN SUCCESS SCENARIO

1. The department manager requests a salary report for their department.
2. The system identifies the department associated with the manager.
3. The system retrieves the current salary information for employees assigned to that department.
4. The system produces a report containing employee identifiers, names and current salaries.
5. The system presents the report to the department manager.

## EXTENSIONS

2. **Manager has no department assigned**:
    1. The system informs the manager that no department is associated with their account.
    2. No report is produced.

3. **Salary data is unavailable**:
    1. The system informs the manager that current salary information cannot be retrieved.
    2. No report is produced.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0.
