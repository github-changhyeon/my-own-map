<template>
  <div class="container pl-15 ml-6">
    <svg viewBox="0 0 1418 116" version="1.1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      <title>@WebDesignerMag</title>
      <g stroke="none" fill="none" fill-rule="evenodd" fill-opacity="1">
        <text id="@WebDesignerMag" stroke="#FF80BC" fill="#" font-weight="normal" font-family="PermanentMarker-Regular, Permanent Marker" font-size="144">
          <tspan x="3" y="109">
            <!--
					-->
            <tspan>@</tspan>
            <!--
					-->
            <tspan>M</tspan>
            <!--
					-->
            <tspan>y</tspan>
            <!--
					-->
            <tspan></tspan>
            <!--
					-->
            <tspan>O</tspan>
            <!--
					-->
            <tspan>w</tspan>
            <!--
					-->
            <tspan>n</tspan>
            <!--
					-->
            <tspan></tspan>
            <!--
					-->
            <tspan>M</tspan>
            <!--
					-->
            <tspan>a</tspan>
            <!--
					-->
            <tspan>p</tspan>
            <!--
					-->
            <tspan>!</tspan>
            <!--
				-->
          </tspan>
        </text>
      </g>
    </svg>
  </div>
</template>

<script>
export default {};
</script>

<style>
@import url('https://fonts.googleapis.com/css?family=Permanent+Marker');
/* .container {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 200px;
    } */
.container svg {
  height: auto;
  fill: none;
  stroke: white;
  stroke-width: 4;
}
.container svg tspan > tspan {
  stroke-dasharray: 1500;
  stroke-dashoffset: -1500;
}
.container svg tspan > tspan:nth-of-type(1) {
  animation: draw 300ms 150ms forwards;
}
.container svg tspan > tspan:nth-of-type(2) {
  animation: draw 300ms 300ms forwards;
}
.container svg tspan > tspan:nth-of-type(3) {
  animation: draw 300ms 450ms forwards;
}
.container svg tspan > tspan:nth-of-type(4) {
  animation: draw 300ms 600ms forwards;
}
.container svg tspan > tspan:nth-of-type(5) {
  animation: draw 300ms 750ms forwards;
}
.container svg tspan > tspan:nth-of-type(6) {
  animation: draw 300ms 900ms forwards;
}
.container svg tspan > tspan:nth-of-type(7) {
  animation: draw 300ms 1050ms forwards;
}
.container svg tspan > tspan:nth-of-type(8) {
  animation: draw 300ms 1200ms forwards;
}
.container svg tspan > tspan:nth-of-type(9) {
  animation: draw 300ms 1350ms forwards;
}
.container svg tspan > tspan:nth-of-type(10) {
  animation: draw 300ms 1500ms forwards;
}
.container svg tspan > tspan:nth-of-type(11) {
  animation: draw 300ms 1650ms forwards;
}
.container svg tspan > tspan:nth-of-type(12) {
  animation: draw 300ms 1800ms forwards;
}
/* .container svg tspan > tspan:nth-of-type(13) {
  animation: draw 300ms 1950ms forwards;
}
.container svg tspan > tspan:nth-of-type(14) {
  animation: draw 300ms 2100ms forwards;
}
.container svg tspan > tspan:nth-of-type(15) {
  animation: draw 300ms 2250ms forwards;
} */
@keyframes draw {
  100% {
    stroke-dashoffset: 0;
  }
}
</style>
