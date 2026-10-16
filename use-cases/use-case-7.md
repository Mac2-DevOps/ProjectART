# USE CASE: 7 Update Employee Details

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to update employee details* so that *the employee database remains accurate and up to date.*

### Scope

Company.

### Level

Primary task.

### Preconditions

The HR advisor has permission to update employee records. The employee record exists.

### Success End Condition

The employee record is updated with the validated information.

### Failed End Condition

The employee record remains unchanged.

### Primary Actor

HR Advisor.

### Trigger

The HR advisor receives updated information about an employee.

## MAIN SUCCESS SCENARIO

1. The HR advisor selects the option to update employee details.
2. The system requests an employee identifier.
3. The HR advisor enters the identifier.
4. The system retrieves and displays the employee record.
5. The HR advisor edits the relevant details.
6. The system validates the updated information.
7. The system saves the changes.
8. The system confirms that the employee record has been updated.

## EXTENSIONS

4. **Employee does not exist**:
    1. The system informs the HR advisor that no matching employee was found.
    2. No record is changed.

6. **Updated details are invalid**:
    1. The system identifies the invalid details.
    2. The HR advisor corrects the information.
    3. The use case resumes at step 6.

7. **Changes cannot be saved**:
    1. The system informs the HR advisor that the update failed.
    2. The original record remains unchanged.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0.
