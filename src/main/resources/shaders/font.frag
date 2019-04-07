uniform sampler2D texture1;

void main(void) {
	gl_FragColor = vec4(1, 1, 1, texture2D(texture1, gl_TexCoord[0].st).r) * gl_Color;
}