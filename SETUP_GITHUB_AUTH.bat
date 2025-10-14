@echo off
echo ========================================
echo GitHub Authentication Setup for Windows
echo ========================================
echo.
echo This will open GitHub to create a Personal Access Token (PAT)
echo The token will be stored permanently in Windows Credential Manager
echo.
echo STEP 1: Opening GitHub Settings...
start https://github.com/settings/tokens/new?description=Windsurf-MorseCode-App&scopes=repo,workflow
echo.
echo ========================================
echo INSTRUCTIONS:
echo ========================================
echo 1. GitHub will open in your browser
echo 2. You'll see "New personal access token (classic)"
echo 3. Token name is already filled: "Windsurf-MorseCode-App"
echo 4. Expiration: Choose "No expiration" or "90 days"
echo 5. Scopes are already selected (repo, workflow)
echo 6. Click "Generate token" at the bottom
echo 7. COPY the token (starts with ghp_...)
echo 8. Come back here and PASTE it below
echo ========================================
echo.
set /p TOKEN="Paste your GitHub token here and press Enter: "
echo.
echo STEP 2: Configuring Git to use credential manager...
git config --global credential.helper manager-core
echo.
echo STEP 3: Testing connection and storing credentials...
echo.
echo When prompted for password, paste your token again!
echo Username: kreggscode
echo Password: [PASTE YOUR TOKEN]
echo.
git ls-remote https://github.com/kreggscode/Morsecode-AI-Android.git
echo.
echo ========================================
echo DONE! Your GitHub credentials are now stored permanently!
echo You won't need to enter them again.
echo ========================================
pause
