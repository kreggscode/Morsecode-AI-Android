@echo off
echo ========================================
echo Pushing Release v1.1.0 to GitHub
echo ========================================
echo.

echo STEP 1: Adding all changes...
git add .

echo.
echo STEP 2: Committing changes...
git commit -m "Release v1.1.0 - Major UI/UX revamp with new features" -m "- Updated version to 1.1.0 (versionCode 10)" -m "- Complete UI/UX redesign with glassmorphism" -m "- Added Encyclopedia, Settings, Onboarding screens" -m "- Enhanced Translator with AI analysis" -m "- New app icon and screenshots" -m "- Android 15/16 compatibility" -m "- Removed debug symbols for release build" -m "- Added GitHub Pages documentation"

echo.
echo STEP 3: Pushing to GitHub...
git push origin main

echo.
echo ========================================
if %ERRORLEVEL% EQU 0 (
    echo SUCCESS! Your code is now on GitHub!
    echo Visit: https://github.com/kreggscode/Morsecode-AI-Android
) else (
    echo FAILED! You need to set up authentication first.
    echo Run SETUP_GITHUB_AUTH.bat first!
)
echo ========================================
pause
