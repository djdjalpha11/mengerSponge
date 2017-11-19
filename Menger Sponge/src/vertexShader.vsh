#version 120
attribute vec3 position;
uniform mat4 modelView;
uniform mat4 proj;
void main()
{
    gl_Position = proj * modelView *  vec4(position, 1.0); // See how we directly give a vec3 to vec4's constructor
}
