package co.uk.genesisengineers.core.visualisation;

import org.lwjgl.opengl.GL20;
import co.uk.genesisengineers.core.util.FileLoader;
import co.uk.genesisengineers.core.util.Logger;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram {

    private int vertexId;
    private int fragmentId;
    private int programmeId;

    ShaderProgram () {
    }

    public void useProgram () {
        GL20.glUseProgram(this.programmeId);
    }

    public int getProgrammeId () {
        return this.programmeId;
    }

    public boolean createProgramme (String vertexFile, String fragmentFile) {
        this.vertexId = this.loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
        this.fragmentId = this.loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);

        if (this.vertexId == -1 || this.fragmentId == -1) {
            Logger.error("createProgramme failed");
            return false;
        }

        programmeId = GL20.glCreateProgram();
        GL20.glAttachShader(programmeId, vertexId);
        GL20.glAttachShader(programmeId, fragmentId);

        glLinkProgram(programmeId);
        glValidateProgram(programmeId);

        if (glGetProgrami(programmeId, GL_VALIDATE_STATUS) == GL_FALSE) {
            Logger.error("shaderProgram GL_VALIDATE_STATUS = false");
        }


        return true;
    }

    public void delete () {
        GL20.glDeleteShader(this.vertexId);
        GL20.glDeleteShader(this.fragmentId);
        GL20.glDeleteProgram(this.programmeId);
    }

    private int loadShader (String filename, int type) {
        int shaderID = 0;

        String source = FileLoader.loadFileAsString(filename);
        if (source == null) {
            return -1;
        }
        shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, source);
        GL20.glCompileShader(shaderID);

        return shaderID;
    }
}
