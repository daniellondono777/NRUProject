# NRUProject
>> It is a page replacement algorithm. This algorithm removes a page at random from the lowest numbered non-empty class. Implicit in this algorithm is that it is better to remove a modified page that has not been referenced in atleast one clock tick than a clean page that is in heavy use.

It is easy to understand, moderately efficient to implement and gives a performance that while certainly not optimal, may be adequate. When page is modified, a modified bit is set. When a page needs to be replaced, the Operating System divides pages into 4 classes.

0:- Not Referenced, Not Modified
1:- Not Referenced, Modified
2:- Referenced, Not Modified
3:- Referenced, Modified
Out of above 4 categories, NRU will replace a Not Referenced, Not Modified page, if such page exists. Note that this algorithm implies that a Modified but Not Referenced is less important than a Not Modified and Referenced.
