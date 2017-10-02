#include <stdio.h> 
#include <fcntl.h> 

/* 

   The program waits for input from the keyboard before writting into 
   the file. Afterwards, it asks for input again and writes again.
   
   I suggest you use the same flags I do when opening a file 
    
   save this program into a file writer.c and compile with 
       cc -o writer writer.c

   then run in by first typing the reader (the other sample program) 
       reader &

   which is placed in the background, then start the writer 
   (this program ) by typing
       writer

   Do NOT run this one in the background (otherwise it will not
   be able to receive input form the keyboard).

   When it asks for input type anything you want (even a simple
   return) You can wait as long as you like before typing something

   after running the program remove the temporary file tempfile
       rm tempfile

   before logging out make sure you do not leave any processes running
   in the background. The unix commands on the web page tell you how
   */
 
main() 
{ 

  int fd,i, j, oflag;
  char buf[1000];

    printf("parent: waiting for input ->:");
    i = getchar();
    oflag = (O_TRUNC | O_CREAT | O_WRONLY);
    fd = open("tempfile", oflag, 0x1c0);
    if (fd < 0 ) {printf("Error opening\n"); exit();}
    printf("parent: writting to file\n");
    write(fd, "This is a test", 15); 
    printf("parent: waiting for input ->:");
    i = getchar();
    write(fd, "Second test", 12); 
    close(fd);
    printf("parent: done\n");
}






