[33mcommit 1fe9edaec6ef3f12083a30b0e8a1d584018a3769[m[33m ([m[1;36mHEAD[m[33m -> [m[1;32mmaster[m[33m, [m[1;31morigin/master[m[33m)[m
Author: Deivi <deivi_arismendi@soy.sena.edu.co>
Date:   Fri Jun 20 14:07:25 2025 -0500

    docs: Actualización y alineación de documentación funcional y técnica (casos de uso, diagramas, instrucciones Copilot)

[33mcommit 969d3848c9a7c1a4acc82788a7c5468f8efa7125[m
Author: Deivi <deivi_arismendi@soy.sena.edu.co>
Date:   Thu Jun 12 16:07:21 2025 -0500

    fix: actualizar configuración VS Code con correcciones y buenas prácticas
    
    ✅ Correcciones aplicadas:
    - Reemplazar java.home obsoleto por java.jdt.ls.java.home
    - Corregir java.completion.guessMethodArguments con valor válido
    - Fijar spring.initializr.defaultPackaging a 'JAR' (mayúsculas)
    - Eliminar configuraciones obsoletas de GitHub Copilot
    - Mantener compatibilidad total con Java 17
    
    🎯 Mejoras aplicadas:
    - Configuración optimizada para el servidor MCP integrado
    - Validaciones JSON correctas para VS Code 1.101+
    - Compatibilidad con las nuevas funcionalidades de GitHub Copilot Chat
    
    📋 Tests verificados: ✅ 21 pruebas pasaron exitosamente
