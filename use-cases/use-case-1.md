# USE CASE: 1 Produce a Report on the Salary of All Employees

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to produce a report on the salary of all employees* so that *I can support financial reporting of the organisation.*

### Scope

Company.

### Level

Primary task.

### Preconditions

The database contains current employee salary data.

### Success End Condition

A report containing the current salaries of all employees is available for HR to provide to finance.

### Failed End Condition

No report is produced.

### Primary Actor

HR Advisor.

### Trigger

A request for organisation-wide salary information is sent to HR.

## MAIN SUCCESS SCENARIO

1. Finance requests salary information for all employees.
2. The HR advisor selects the option to report salaries for all employees.
3. The HR advisor retrieves the current salary information for all employees.
4. The system produces a report containing employee identifiers, names and current salaries.
5. The HR advisor provides the report to finance.

## EXTENSIONS

3. **Salary data is unavailable**:
    1. The system informs the HR advisor that current salary information cannot be retrieved.
    2. No report is produced.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0.
