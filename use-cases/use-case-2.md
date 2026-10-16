# USE CASE: 2 Produce a Report on the Salary of Employees in a Department

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to produce a report on the salary of employees in a department* so that *I can support departmental financial reporting.*

### Scope

Company.

### Level

Primary task.

### Preconditions

The department is known. The database contains current employee salary data and department assignments.

### Success End Condition

A report containing the current salaries of employees in the specified department is available to HR.

### Failed End Condition

No report is produced.

### Primary Actor

HR Advisor.

### Trigger

A request for departmental salary information is sent to HR.

## MAIN SUCCESS SCENARIO

1. Finance requests salary information for a particular department.
2. The HR advisor captures or selects the department.
3. The system retrieves the current salary information for employees assigned to that department.
4. The system produces a report containing employee identifiers, names and current salaries.
5. The HR advisor provides the report to the requester.

## EXTENSIONS

2. **Department does not exist**:
    1. The system informs the HR advisor that the department could not be found.
    2. No report is produced.

3. **Salary data is unavailable**:
    1. The system informs the HR advisor that current salary information cannot be retrieved.
    2. No report is produced.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0.
