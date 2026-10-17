# USE CASE: 5 Add a New Employee

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to add new employee details to the system* so that *the organisation maintains an accurate employee record.*

### Scope

Company.

### Level

Primary task.

### Preconditions

The HR advisor has permission to maintain employee records. The required details for the new employee are available.

### Success End Condition

A new employee record is stored in the database.

### Failed End Condition

No employee record is created.

### Primary Actor

HR Advisor.

### Trigger

A new employee joins the organisation and their details must be recorded.

## MAIN SUCCESS SCENARIO

1. The HR advisor selects the option to add a new employee.
2. The system requests the required employee details.
3. The HR advisor enters the employee details.
4. The system validates the supplied information.
5. The system stores the new employee record.
6. The system confirms that the employee record has been created.

## EXTENSIONS

4. **Required details are missing or invalid**:
    1. The system identifies the missing or invalid details.
    2. The HR advisor corrects the information.
    3. The use case resumes at step 4.

5. **Employee record cannot be saved**:
    1. The system informs the HR advisor that the record could not be created.
    2. No successful creation is reported.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0.
