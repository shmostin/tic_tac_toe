#include <stdio.h>
#include <ulfius.h>
#include <unistd.h>
#include <time.h>

#define PORT 8080
int shutdown_now = 0;
int customer_id = 0;
int board[9] = {0, 0, 0, 0, 0, 0, 0, 0, 0};

int machine_strategy(int board[]);

/**
processes the shut down of the server when <ENTER> is pushed
**/
int process_shutdown(const struct _u_request * request, struct _u_response * response, void * user_data) {
  shutdown_now = 1;

  return U_CALLBACK_CONTINUE;
}

/**
This function proccesses POST requests from the client
input: json gameboard from the client
return: a new jason gameboard with a move made
**/
int Json_process(const struct _u_request * request, struct _u_response * response, void * user_data) {
  printf("started json_processor\n");
  json_auto_t * json_input = ulfius_get_json_body_request(request, NULL);

  if(!json_input) {
    printf("Json_input was null\n");
  }
  json_auto_t * input_s1;
  json_auto_t * input;
  json_auto_t * json_reply = json_object();

  // printf("%s\n", json_input);

  input_s1 = json_object_get(json_input, "s1");
  if (json_is_integer(input_s1)) {
    printf("input_s1 value is int\n");
  }
  // else {
  //   printf("input_id value is NOT an int\n");
  // }
  board[0] = json_integer_value(json_object_get(json_input, "s1"));
  board[1] = json_integer_value(json_object_get(json_input, "s2"));
  board[2] = json_integer_value(json_object_get(json_input, "s3"));
  board[3] = json_integer_value(json_object_get(json_input, "s4"));
  board[4] = json_integer_value(json_object_get(json_input, "s5"));
  board[5] = json_integer_value(json_object_get(json_input, "s6"));
  board[6] = json_integer_value(json_object_get(json_input, "s7"));
  board[7] = json_integer_value(json_object_get(json_input, "s8"));
  board[8] = json_integer_value(json_object_get(json_input, "s9"));




  int machines_move = machine_strategy(board);
  board[machines_move] = 1;

//put the board back into json object
  json_auto_t *s1_obj = json_integer(board[0]);

  json_object_set(json_reply, "s1", s1_obj);

  json_auto_t *s2_obj = json_integer(board[1]);
  json_object_set(json_reply, "s2", s2_obj);

  json_auto_t *s3_obj = json_integer(board[2]);
  json_object_set(json_reply, "s3", s3_obj);

  json_auto_t *s4_obj = json_integer(board[3]);
  json_object_set(json_reply, "s4", s4_obj);

  json_auto_t *s5_obj = json_integer(board[4]);
  json_object_set(json_reply, "s5", s5_obj);

  json_auto_t *s6_obj = json_integer(board[5]);
  json_object_set(json_reply, "s6", s6_obj);

  json_auto_t *s7_obj = json_integer(board[6]);
  json_object_set(json_reply, "s7", s7_obj);

  json_auto_t *s8_obj = json_integer(board[7]);
  json_object_set(json_reply, "s8", s8_obj);

  json_auto_t *s9_obj = json_integer(board[8]);
  json_object_set(json_reply, "s9", s9_obj);






  ulfius_set_json_body_response(response, 200, json_reply);

  return U_CALLBACK_CONTINUE;
}

/**
* creates the new game board after it randomly decides who will go first.
input: the current game board as a json
return: the new game board to the client
*/
int new_game(const struct _u_request * request, struct _u_response * response, void * user_data) {
  printf("building a new game\n");
  srand(time(NULL));
  int random = rand() % (2) + 1;
  printf("%d\n", random);

  for (int i=0; i<9; i++) {
    board[i] = 0;
  }

  for (int i=0; i<9; i++) {
    printf("%d\n", board[i]);
  }
  //randomly decide who will go first
  if(random == 1) {
    int machines_move = machine_strategy(board);
    board[machines_move] = 1;
  }

  json_auto_t *obj = json_object();
  json_auto_t *s1_obj = json_integer(board[0]);
  json_object_set(obj, "s1", s1_obj);

  json_auto_t *s2_obj = json_integer(board[1]);
  json_object_set(obj, "s2", s2_obj);

  json_auto_t *s3_obj = json_integer(board[2]);
  json_object_set(obj, "s3", s3_obj);

  json_auto_t *s4_obj = json_integer(board[3]);
  json_object_set(obj, "s4", s4_obj);

  json_auto_t *s5_obj = json_integer(board[4]);
  json_object_set(obj, "s5", s5_obj);

  json_auto_t *s6_obj = json_integer(board[5]);
  json_object_set(obj, "s6", s6_obj);

  json_auto_t *s7_obj = json_integer(board[6]);
  json_object_set(obj, "s7", s7_obj);

  json_auto_t *s8_obj = json_integer(board[7]);
  json_object_set(obj, "s8", s8_obj);

  json_auto_t *s9_obj = json_integer(board[8]);
  json_object_set(obj, "s9", s9_obj);

  ulfius_set_json_body_response(response, 200, obj);

  return U_CALLBACK_CONTINUE;
}



/**
The main function that wais for http requests from clients
input: none
return: boards to clients
**/

int main(void) {
  struct _u_instance instance;

  //Initialize this instance with the port Number
  if (ulfius_init_instance(&instance, PORT, NULL, NULL) != U_OK) {
    fprintf(stderr, "Error ulfius_init_instance, ABORT\n");
    return(1);
  }

  //Endpoint newGame declaration
  ulfius_add_endpoint_by_val(&instance, "GET", "/newGame", NULL, 0, &new_game, NULL);

  //Endpoint yourTurn declaration
  ulfius_add_endpoint_by_val(&instance, "POST", "/yourTurn", NULL, 0, &Json_process, NULL);

  //Register the shutdown callback
  ulfius_add_endpoint_by_val(&instance, "POST", "/shutdown", NULL, 0, &process_shutdown, NULL);
  //start the framework
  if (ulfius_start_framework(&instance) == U_OK) {
    printf("start frame work on port %d\n", instance.port);

    //wait for the user to press <enter> on the console to quit the appropriate
    getchar();



  } else {
    fprintf(stderr, "Error starting the framework\n");
  }
  printf("End framework\n");
  ulfius_stop_framework(&instance);
  ulfius_clean_instance(&instance);

  return 0;

}
