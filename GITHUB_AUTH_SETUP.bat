@echo off
echo Setting up GitHub authentication...
echo.
echo INSTRUCTIONS:
echo 1. Go to https://github.com/settings/tokens
echo 2. Click "Generate new token (classic)"
echo 3. Give it a name like "Morse Code Push"
echo 4. Select scopes: check "repo"
echo 5. Click "Generate token"
echo 6. Copy the token
echo 7. Run this command: git push https://YOUR_USERNAME:YOUR_TOKEN@github.com/kreggscode/Morsecode-AI-Android.git main
echo.
echo Replace YOUR_USERNAME with your GitHub username
echo Replace YOUR_TOKEN with the token you generated
echo.
pause
