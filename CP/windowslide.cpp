#include <bits/stdc++.h>
using namespace std;

/*
 --------------------------------------------------------
  Sliding Window Problem 1:
  Find the maximum sum of any contiguous subarray of size k
 --------------------------------------------------------
*/
int sliding(const vector<int> &arr, int k)
{
    int sum = 0, maxsum = 0;

    // Compute initial window sum (first k elements)
    for (int i = 0; i < k; i++)
        sum += arr[i];

    maxsum = sum;

    // Slide the window by 1 element at a time
    for (int i = k; i < arr.size(); i++)
    {
        sum += arr[i];        // Add new element
        sum -= arr[i - k];    // Remove old element
        maxsum = max(maxsum, sum);
    }

    return maxsum;
}

/*
 --------------------------------------------------------
  Sliding Window Problem 2:
  Longest substring without repeating characters
 --------------------------------------------------------
*/
int longuest(string s)
{
    int freq[256] = {0}; // Frequency array for ASCII characters
    int start = 0, lon = 0;

    for (int i = 0; i < s.length(); i++)
    {
        freq[s[i]]++;

        // If character repeated, move left pointer
        while (freq[s[i]] > 1)
        {
            freq[s[start]]--;
            start++;
        }

        // Update longest length
        lon = max(lon, i - start + 1);
    }
    return lon;
}

/*
 --------------------------------------------------------
  Sliding Window Problem 3:
  Smallest subarray with sum >= target t
 --------------------------------------------------------
*/
int smallest(const vector<int> &arr, int t)
{
    int start = 0, lon = INT_MAX, sum = 0;

    for (int i = 0; i < arr.size(); i++)
    {
        sum += arr[i];

        // shrink window while sum is >= target
        while (sum >= t)
        {
            lon = min(lon, i - start + 1);
            sum -= arr[start];
            start++;
        }
    }

    return lon == INT_MAX ? 0 : lon; // 0 if no valid window
}

/*
 --------------------------------------------------------
  Sliding Window Problem 4:
  Longest substring containing at most k distinct characters
 --------------------------------------------------------
*/
int longuestkdistinct(string s, int k)
{
    unordered_map<char, int> freq;
    int start = 0, lon = 0, distinct = 0;

    for (int i = 0; i < s.size(); i++)
    {
        if (freq[s[i]] == 0)
            distinct++;

        freq[s[i]]++;

        // If too many distinct characters, shrink the window
        while (distinct > k)
        {
            if (freq[s[start]] == 1)
                distinct--;
            freq[s[start]]--;
            start++;
        }

        lon = max(lon, i - start + 1);
    }

    return lon;
}

/*
 --------------------------------------------------------
  MAIN FUNCTION
 --------------------------------------------------------
*/
int main()
{
    // Sliding window test for max sum of size k
    vector<int> arr = {2, 1, 5, 1, 3, 2};
    int k = 3;
    cout << "Max sum of a subarray of size " << k << " = "
         << sliding(arr, k) << endl;

    // Test longest substring without repeating characters
    string s1 = "abcabcbb";
    cout << "Longest substring without repeating characters = "
         << longuest(s1) << endl;

    // Test smallest subarray with sum >= target
    vector<int> arr2 = {2, 3, 1, 2, 4, 3};
    int target = 7;
    cout << "Smallest subarray with sum >= " << target << " = "
         << smallest(arr2, target) << endl;

    // Test longest substring with k distinct characters
    string s2 = "eceba";
    int k2 = 2;
    cout << "Longest substring with at most " << k2 << " distinct characters = "
         << longuestkdistinct(s2, k2) << endl;

    return 0;
}
