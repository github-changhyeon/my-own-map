<template>
  <v-row justify="center">
    <v-col cols="12" sm="8">
      <v-card>
        <v-card-title class="cyan darken-1">
          <span class="headline white--text">정보 변경하기</span>

          <v-spacer></v-spacer>
        </v-card-title>
        <v-divider inset></v-divider>
        <v-list>
          <v-list-item style="margin-bottom: 5px;">
            <v-list-item-action>
              <v-icon>mdi-email</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>ssafy4@ssfay.com</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-divider inset></v-divider>

          <v-list-item>
            <v-list-item-action>
              <v-icon>mdi-lock-outline</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>***********</v-list-item-title>
            </v-list-item-content>

            <v-list-item-action>
              <v-btn>변경</v-btn>
            </v-list-item-action>
          </v-list-item>

          <v-divider inset></v-divider>

          <v-list-item>
            <v-list-item-action>
              <v-icon>mdi-account</v-icon>
            </v-list-item-action>

            <v-list-item-content>
              <v-list-item-title>싸피인</v-list-item-title>
            </v-list-item-content>

            <v-list-item-action>
              <v-btn>변경</v-btn>
            </v-list-item-action>
          </v-list-item>
        </v-list>
      </v-card>
    </v-col>
  </v-row>
</template>
