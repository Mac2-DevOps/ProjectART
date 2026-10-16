# USE CASE: 6 View Employee Details

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to view employee details* so that *I can access accurate information when carrying out HR duties.*

### Scope

Company.

### Level

Primary task.

### Preconditions

The HR advisor has permission to view employee records. The employee can be identified.

### Success End Condition

The requested employee details are displayed.

### Failed End Condition

The requested employee details are not displayed.

### Primary Actor

HR Advisor.

### Trigger

The HR advisor needs to look up an employee record.

## MAIN SUCCESS SCENARIO

1. The HR advisor selects the option to view employee details.
2. The system requests an employee identifier or other supported search criteria.
3. The HR advisor enters the search information.
4. The system searches the employee database.
5. The system displays the matching employee details.

## EXTENSIONS

4. **Employee does not exist**:
    1. The system informs the HR advisor that no matching employee was found.
    2. No employee details are displayed.

4. **Multiple employees match the search criteria**:
    1. The system displays the matching employees.
    2. The HR advisor selects the required employee.
    3. The system displays that employee's details.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0.
