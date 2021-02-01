<template>
  <v-row justify="center">
    <v-dialog
      v-model="title"
      persistent
      max-width="350"
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          color="primary"
          dark
          v-bind="attrs"
          v-on="on"
        >
          +
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="headline">
          새로운 hash를 등록해주세요.
        </v-card-title>
        <input type="text" v-model.trim="title" @keypress.enter="addHash">
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="green darken-1"
            text
            @click="addHash"
          >
            추가
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import axios from'axios'

export default {
  name: 'HashModal',
  data: function () {
    return {
      title: '',
    }
  },
  methods: {
    addHash() {
      const newHash = {
        title: this.title,
      }

      if (newHash.title) {
        axios.post('url', newHash)
          .then((res) => {
            console.log(res)
          })
          .catch((err) => {
            console.log(err)
          })
        }
    },
  }
}
</script>