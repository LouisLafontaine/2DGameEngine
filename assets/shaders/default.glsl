#type vertex
#version 330 core

layout (location = 0) in vec3 aPos; // letter a in front is for attribute
layout (location = 1) in vec4 aColor;

out vec4 fColor; // letter f is for fragment shader

void main() {
    fColor = aColor;
    gl_Position = vec4(aPos, 1.0);
}

#type fragment
#version 330 core

in vec4 fColor;

out vec4 color;

void main() {
    color = fColor;
}