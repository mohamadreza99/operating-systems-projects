# Operating Systems Projects

## 1. Threading
This projects is an illustration of how threads, locks, and semaphores work. In this example, we have an intersection in which cars want to pass it. Every area of this intersection (NE, NW, SW, SE) has a lock seperately. every car arrives at the intersection, has a source and a destination road, so it is clear which areas of the intersection should be without any other car to permit the very arrived car to pass. Hence, if a car want to pass an area line NE in its way, it must aquire that semaphore.

<img width="468" alt="Screen Shot 1400-07-08 at 11 23 06 AM" src="https://user-images.githubusercontent.com/39591768/135410901-b4f9b9b9-dff3-4447-9d47-a5c35497715b.png">

I implemented this project using java thread synchoronization and thread locks which they serve as four areas in the intersection that I mentioned above.

## 2. Paging and Memory Management
In this project, paging system of os is implemented. we have a backstore.bin file that it is as the physical memory. We have a virtual memory for processes running in the os and each virtual address should be translated to the related physical address using page table and TLB. For more details look at this [readme](https://github.com/mohamadreza99/operating-systems-projects/blob/main/2.OSPaging/README.md) file located in the directory of this project.


