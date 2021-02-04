<template>
  <v-col md="4" offset-md="4">
    <v-dialog ref="dialog" v-model="dateModal" :return-value.sync="date" persistent lazy full-width width="290px">
      <template v-slot:activator="{ on }">
        <v-text-field v-model="date" :label="`${label}`" prepend-icon="mdi-calendar-range" readonly v-on="on"></v-text-field>
      </template>
      <v-date-picker v-model="date" scrollable multiple>
        <v-spacer></v-spacer>
        <v-btn text color="primary" @click="dateModal = false">Cancel</v-btn>
        <v-btn text color="primary" @click="set()">OK</v-btn>
      </v-date-picker>
    </v-dialog>
  </v-col>
</template>

<script>
export default {
  name: 'DatePicker',
  props: ['label'],
  data: () => ({
    date: '',
    dateModal: false,
  }),
  methods: {
    set() {
      this.$refs.dialog.save(this.date);
      this.$emit('setDate', this.date[1]);
    },
  },
};
</script>
