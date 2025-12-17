import pytest
from datetime import datetime

# -------------------------
# Function under test
# -------------------------
def multiple_of_two(num):
    if num == 0:
        raise ValueError
    return num % 2 == 0


# -------------------------
# Test 1 — True & False cases
# -------------------------
def test_numbers():
    assert multiple_of_two(2) is True
    # "False" test
    assert multiple_of_two(1) is False


# -------------------------
# Test 2 — Exception case
# -------------------------
def test_zero():
    # context manager for exception
    with pytest.raises(ValueError):
        multiple_of_two(0)


# -------------------------
# Test 3 — Expected failure
# -------------------------
@pytest.mark.xfail
def test_fails():
    # This assertion is deliberately wrong
    assert multiple_of_two(2) == True  # Change to False if you want clear failure


# -------------------------
# Additional Function
# -------------------------
def get_unique_values(lst):
    return list(set(lst))


# Prepare condition for skip
day_of_week = datetime.now().isoweekday()  # 1=Mon … 7=Sun
condition_string = "day_of_week == 6"      # skip if Saturday


# -------------------------
# Test 4 — Conditional skip
# -------------------------
@pytest.mark.skipif(eval(condition_string), reason="Skipping test on Saturday")
def test_function():
    assert sorted(get_unique_values([1, 2, 3])) == [1, 2, 3]
    assert sorted(get_unique_values([1, 2, 3, 1])) == [1, 2, 3]
