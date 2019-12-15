uniform mat4 ModelViewProjection;

attribute vec2 vert_TexCoord;
attribute vec3 vert_Position;

varying vec2 TexCoord;

void main()
{
	TexCoord = vert_TexCoord;
	gl_Position = ModelViewProjection * vec4(vert_Position, 1.0);
}
