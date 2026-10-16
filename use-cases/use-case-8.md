# USE CASE: 8 Delete an Employee Record

## CHARACTERISTIC INFORMATION

### Goal in Context

As an *HR advisor* I want *to delete an employee record* so that *the system does not retain employee records that should be removed.*

### Scope

Company.

### Level

Primary task.

### Preconditions

The HR advisor has permission to delete employee records. The employee record exists, and the deletion is authorised under organisational policy.

### Success End Condition

The employee record is deleted or otherwise removed in accordance with the organisation's record-retention policy.

### Failed End Condition

The employee record is not deleted.

### Primary Actor

HR Advisor.

### Trigger

The HR advisor receives an authorised request to remove an employee record.

## MAIN SUCCESS SCENARIO

1. The HR advisor selects the option to delete an employee record.
2. The system requests an employee identifier.
3. The HR advisor enters the identifier.
4. The system retrieves and displays the employee record.
5. The system asks the HR advisor to confirm the deletion.
6. The HR advisor confirms the deletion.
7. The system deletes or removes the record in accordance with the applicable retention policy.
8. The system confirms that the record has been removed.

## EXTENSIONS

4. **Employee does not exist**:
    1. The system informs the HR advisor that no matching employee was found.
    2. No record is deleted.

6. **Deletion is cancelled**:
    1. The HR advisor cancels the operation.
    2. The system leaves the employee record unchanged.

7. **Record cannot be deleted**:
    1. The system informs the HR advisor that the deletion failed.
    2. The record remains unchanged.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 1.0.
