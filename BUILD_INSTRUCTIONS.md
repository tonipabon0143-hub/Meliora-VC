# Meliora VC - Build Instructions

## Prerequisites

Before building, install these on your computer:

### 1. Install Java JDK 11+
- Download: https://www.oracle.com/java/technologies/downloads/
- Choose JDK (not JRE)
- Install to default location
- Verify: Open terminal and type `java -version`

### 2. Install Maven
- Download: https://maven.apache.org/download.cgi
- Extract to a folder (e.g., `C:\maven` or `~/maven`)
- Add to PATH environment variable
- Verify: Open terminal and type `mvn -version`

## Building the Plugin (Local)

### Windows:

1. Extract this folder
2. Open Command Prompt
3. Navigate to folder: `cd C:\path\to\Meliora-VC`
4. Run: `build.bat`
5. JAR will be in: `target\Meliora-VC-1.0.0.jar`

### Mac/Linux:

1. Extract this folder
2. Open Terminal
3. Navigate to folder: `cd /path/to/Meliora-VC`
4. Make script executable: `chmod +x build.sh`
5. Run: `./build.sh`
6. JAR will be in: `target/Meliora-VC-1.0.0.jar`

### Manual Build (Any OS):

1. Open terminal/command prompt in this folder
2. Run: `mvn clean package`
3. Wait for "BUILD SUCCESS"
4. Find JAR in `target/` folder

## GitHub Actions Build (Recommended)

### Step 1: Create GitHub Repository

1. Go to https://github.com/new
2. Repository name: `Meliora-VC`
3. Description: `Server-side voice chat plugin for Minecraft`
4. Public (checked)
5. Create repository

### Step 2: Upload These Files to GitHub

Using Git:
```bash
git init
git add .
git commit -m "Initial commit: Meliora VC plugin"
git branch -M main
git remote add origin https://github.com/YOUR-USERNAME/Meliora-VC.git
git push -u origin main
```

### Step 3: GitHub Actions Builds Automatically

1. Go to your repository: `https://github.com/YOUR-USERNAME/Meliora-VC`
2. Click **Actions** tab
3. Wait 2-3 minutes for build to complete
4. Green checkmark = success ✅

### Step 4: Download JAR

1. Go to **Actions** tab
2. Click latest workflow run
3. Scroll to **Artifacts** section
4. Download **Meliora-VC-JAR**
5. Extract and use JAR file

## Installation on Server

1. Copy `Meliora-VC-1.0.0.jar`
2. Paste into `server/plugins/` folder
3. Restart server
4. Check console for "✓ Enabled successfully!"

## Troubleshooting

### Build fails - "Maven not found"
- Maven not installed or not in PATH
- Reinstall Maven and add to PATH

### Build fails - "Java version error"
- Java 11+ not installed
- Install latest JDK from oracle.com

### BUILD SUCCESS but no JAR
- Check `target/` folder
- Should be named `Meliora-VC-1.0.0.jar`

### Plugin doesn't load
- JAR in correct folder: `plugins/`
- Server restarted
- Check console for errors

---

Happy building! 🎮