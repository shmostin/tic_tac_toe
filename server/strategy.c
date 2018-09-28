#include <stdio.h>


#define GAME_SIZE 9;

//Strategy to winning
//1)win if possible
//2)Block if possible
//3)Fork, create two win possibilities
//4)Block opponents Fork/
//5)center
//6)opposite corners
//7)empty corners
//8)empty SIDE
// I found the above strategy information on the wikipedia Tic-tac-toe page

int machine_strategy(int board[]) {

  //1) win if possible
  //check the columns
  for (int i=0; i<3; i++) {
    if (board[i] == 0 && board[i+3] == 1 && board[i+6] == 1) {
      return i;
    }
    if (board[i] == 1 && board[i+3] == 0 && board[i+6] == 1) {
      return i + 3;
    }
    if (board[i] == 1 && board[i+3] == 1 && board[i+6] == 0) {
      return i + 6;
    }
  }

  //check the rows
  for(int i=0; i<7; i+=3) {
    if (board[i] == 0 && board[i+1] == 1 && board[i+2] == 1) {
      return i;
  }
    if (board[i] == 1 && board[i+1] == 0 && board[i+2] == 1) {
        return i+1;
  }
    if (board[i] == 1 && board[i+1] == 1 && board[i+2] == 0) {
      return i+2;
  }
}

//diagonal
  if (board[0] == 0 && board[4] == 1 && board[8] == 1) {
    return 0;
  }
  if (board[0] == 1 && board[4] == 0 && board[8] == 1) {
    return 4;
  }
  if (board[0] == 1 && board[4] == 1 && board[8] == 0) {
    return 8;
  }
//diagonal
  if (board[6] == 0 && board[4] == 1 && board[2] == 1) {
    return 6;
  }
  if (board[6] == 1 && board[4] == 0 && board[2] == 1) {
    return 4;
  }
  if (board[6] == 1 && board[4] == 1 && board[2] == 0) {
    return 2;
  }

//2)block the opponent from winning
//check the columns
for (int i=0; i<3; i++) {
  if (board[i] == 0 && board[i+3] == 2 && board[i+6] == 2) {
    return i;
  }
  if (board[i] == 2 && board[i+3] == 0 && board[i+6] == 2) {
    return i + 3;
  }
  if (board[i] == 2 && board[i+3] == 2 && board[i+6] == 0) {
    return i + 6;
  }
}

//check the rows
  for(int i=0; i<7; i+=3) {
    if (board[i] == 0 && board[i+1] == 2 && board[i+2] == 2) {
      return i;
  }
    if (board[i] == 2 && board[i+1] == 0 && board[i+2] == 2) {
        return i+1;
  }
    if (board[i] == 2 && board[i+1] == 2 && board[i+2] == 0) {
      return i+2;
  }
}

//diagonal
  if (board[6] == 0 && board[4] == 2 && board[2] == 2) {
    return 6;
  }
  if (board[6] == 2 && board[4] == 0 && board[2] == 2) {
    return 4;
  }
  if (board[6] == 2 && board[4] == 2 && board[2] == 0) {
    return 2;
  }

  //diagonal
    if (board[0] == 0 && board[4] == 2 && board[8] == 2) {
      return 0;
    }
    if (board[0] == 2 && board[4] == 0 && board[8] == 2) {
      return 4;
    }
    if (board[0] == 2 && board[4] == 2 && board[8] == 0) {
      return 8;
    }

    //3)Fork. Two chances of winning

    //middle row
    if (board[3] == 1 && board[4] == 1) {
      if (board[0] == 0 && board[2] == 0 && board[6] == 0) {
        return 6;
      }
    }
      if (board[4] ==1 && board[5] ==1) {
        if (board[2] == 0 && board[6] == 0 && board[8] ==0) {
          return 2;
        }
      }

      //middle column
      if (board[4] == 1 && board[1] == 1) {
        if (board[0] == 0 && board[2] == 0 && board[8] == 0) {
          return 0;
        }
        if (board[0] == 0 && board[2] == 0 && board[6] == 0) {
          return 2;
        }
      }
        if (board[4] ==1 && board[7] ==1) {
          if (board[2] == 0 && board[6] == 0 && board[8] ==0) {
            return 8;
          }
          if (board[2] == 0 && board[6] == 0 && board[8] ==0) {
            return 6;
          }
        }

        //same corner and same column
        //top left bottom left
        if (board[6] == 1 && board[0] == 1) {
          if (board[1] == 0 && board[4] == 0 && board[2] ==0) {
            return 2;
          }
          if (board[4] == 0 && board[8] == 0 && board[7] == 0) {
            return 8;
          }
          if (board[2] == 0 && board[4] == 0 && board[8] == 0) {
            return 4;
          }
        }

       //top right bottom right
       if (board[2] == 1 && board[8] == 1) {
         if (board[0] == 0 && board[1] == 0 && board[4] == 0) {
           return 0;
         }
         if (board[4] == 0 && board[6] == 0 && board[7] == 0) {
           return 6;
         }
         if (board[0] == 0 && board[4] == 0 && board[6] == 0) {
           return 4;
         }
       }

       //top left top right
        if (board[0] == 1 && board[2] == 1) {
          if (board[3] == 0 && board[4] == 0 && board[6] ==0) {
            return 6;
          }
          if (board[4] == 0 && board[5] == 0 && board[8] ==0) {
            return 8;
          }
          if (board[6] == 0 && board[4] == 0 && board[8] ==0) {
            return 4;
          }
        }


        //bottom left bottom right
        if (board[6] == 1 && board[8] == 1) {
          if (board[0] == 0 && board[3] == 0 && board[4] == 0) {
            return 0;
          }
          if (board[2] == 0 && board[4] == 0 && board[5] ==0) {
            return 2;
          }
          if (board[0] == 0 && board[2] == 0 && board[4] ==0) {
            return 4;
          }
        }


      //4) block opponent fork
    if ((board[0] == 2 && board[8] == 2) || (board[2] == 2 && board[6] == 2)) {
          for (int i=1; i<8; i+= 2) {
            if (board[i] == 0) {
              return i;
            }
          }
    }

     //5) play center
     if (board[4] == 0) {
       return 4;
     }

     //6) opposite corners
     if (board[0] == 2 && board[8] == 0) {
       return 8;
     }
     if (board[2] == 2 && board[6] == 0) {
       return 6;
     }
     if (board[8] == 2 && board[0] == 0) {
       return 0;
     }
     if (board[6] == 2 && board[2] == 0) {
       return 2;
     }

     //7) play any empty corner
     if (board[0] == 0) {
       return 0;
     }
     if (board[2] == 0) {
       return 2;
     }
     if (board[6] == 0) {
       return 6;
     }
     if (board[8] == 0) {
       return 8;
     }

     //8) play outside middle position
     for (int i=0; i<8; i+=2) {
       if (board[i] == 0) {
         return i;
       }
     }

    }
