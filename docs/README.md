# 🎯 Morse Code Master - GitHub Pages Setup

## 📱 About the App

**Morse Code Master** is a comprehensive educational app for learning and translating Morse code. Available on Google Play Store.

[![Get it on Google Play](https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png)](https://play.google.com/store/apps/details?id=com.kreggscode.morsecode)

---

## 🌐 Website Setup Instructions

This directory contains the GitHub Pages website for Morse Code Master.

### Files Structure:
```
docs/
├── index.html          # Main landing page
├── privacy.html        # Privacy policy page
├── screenshots/        # App screenshots folder
│   ├── screenshot1.png
│   ├── screenshot2.png
│   ├── screenshot3.png
│   ├── screenshot4.png
│   ├── screenshot5.png
│   └── screenshot6.png
└── README.md          # This file
```

---

## 🚀 How to Deploy to GitHub Pages

### Step 1: Create GitHub Repository
1. Go to [GitHub](https://github.com)
2. Click "New Repository"
3. Name it: `morse-code-master`
4. Make it **Public**
5. Click "Create Repository"

### Step 2: Upload Files
```bash
# In your project directory
cd "C:\Users\kreg9\Downloads\kreggscode\windsurf\Android Studio projects\Morse code"

# Initialize git (if not already done)
git init

# Add all files
git add docs/

# Commit
git commit -m "Add GitHub Pages website"

# Add remote (replace YOUR_USERNAME with your GitHub username)
git remote add origin https://github.com/YOUR_USERNAME/morse-code-master.git

# Push to GitHub
git push -u origin main
```

### Step 3: Enable GitHub Pages
1. Go to your repository on GitHub
2. Click **Settings** tab
3. Scroll down to **Pages** section (left sidebar)
4. Under **Source**, select:
   - Branch: `main`
   - Folder: `/docs`
5. Click **Save**
6. Wait 2-3 minutes for deployment

### Step 4: Access Your Website
Your website will be available at:
```
https://YOUR_USERNAME.github.io/morse-code-master/
```

Privacy Policy URL:
```
https://YOUR_USERNAME.github.io/morse-code-master/privacy.html
```

---

## 📸 Adding Screenshots

### Step 1: Take Screenshots
1. Open your app on an Android device/emulator
2. Take screenshots of:
   - Main translator screen
   - Learning/Encyclopedia screen
   - Games screen
   - AI chat screen
   - Dark theme view
   - History screen

### Step 2: Prepare Screenshots
1. Resize to consistent dimensions (e.g., 1080x2400)
2. Save as PNG format
3. Name them: `screenshot1.png`, `screenshot2.png`, etc.

### Step 3: Add to Repository
```bash
# Create screenshots folder
mkdir docs/screenshots

# Copy your screenshots to docs/screenshots/
# Then commit and push
git add docs/screenshots/
git commit -m "Add app screenshots"
git push
```

---

## 🔗 Update Play Store Listing

### Privacy Policy URL
Once deployed, add this URL to your Play Store listing:
```
https://YOUR_USERNAME.github.io/morse-code-master/privacy.html
```

### Website URL
Add this to your developer profile:
```
https://YOUR_USERNAME.github.io/morse-code-master/
```

---

## 🎨 Customization

### Update Play Store Link
Edit `index.html` and `privacy.html`:
```html
<!-- Find this line -->
<a href="https://play.google.com/store/apps/details?id=com.kreggscode.morsecode">

<!-- Replace with your actual Play Store URL once published -->
```

### Update Developer Links
Edit footer section in `index.html`:
```html
<!-- Find "More Apps" section -->
<li><a href="https://play.google.com/store/apps/dev?id=YOUR_DEVELOPER_ID" target="_blank">View All Apps</a></li>

<!-- Replace YOUR_DEVELOPER_ID with your actual Google Play Developer ID -->
```

### Add Your GitHub Profile
```html
<!-- Update GitHub links -->
<li><a href="https://github.com/YOUR_USERNAME" target="_blank">GitHub</a></li>
```

---

## 📝 Features of the Website

### ✨ Main Landing Page (`index.html`)
- **Hero Section**: Eye-catching introduction with animated Morse code
- **Features Grid**: 8 feature cards with icons and descriptions
- **Screenshots Slider**: Horizontal scrolling gallery
- **Download Section**: Direct link to Google Play
- **Responsive Design**: Works on mobile, tablet, and desktop
- **Smooth Animations**: Professional fade-in and hover effects
- **Modern UI**: Glassmorphism design with gradients

### 🔒 Privacy Policy (`privacy.html`)
- **Comprehensive**: Covers all privacy aspects
- **User-Friendly**: Easy to read and understand
- **GDPR Compliant**: Meets international privacy standards
- **COPPA Compliant**: Safe for children
- **Professional**: Clean, organized layout
- **Transparent**: Clear explanation of all permissions

---

## 🎯 SEO Optimization

The website includes:
- ✅ Meta descriptions
- ✅ Keywords
- ✅ Semantic HTML
- ✅ Fast loading (no external dependencies except fonts)
- ✅ Mobile-responsive
- ✅ Accessible design

---

## 🔧 Troubleshooting

### Website Not Loading?
1. Check if GitHub Pages is enabled in repository settings
2. Verify files are in `/docs` folder
3. Wait 5-10 minutes after first deployment
4. Clear browser cache

### Screenshots Not Showing?
1. Verify images are in `docs/screenshots/` folder
2. Check file names match HTML references
3. Ensure images are PNG or JPG format
4. Push changes to GitHub

### Privacy Policy Link Not Working?
1. Verify `privacy.html` is in `/docs` folder
2. Check the URL format: `https://username.github.io/repo-name/privacy.html`
3. Wait a few minutes after deployment

---

## 📱 Play Store Integration

### Add Website URL
1. Go to Play Console
2. Select your app
3. Go to **Store Presence** → **Main Store Listing**
4. Add website URL in **Contact Details**

### Add Privacy Policy URL
1. In Play Console
2. Go to **App Content** → **Privacy Policy**
3. Add the GitHub Pages privacy policy URL
4. Save changes

---

## 🌟 More Apps by KreggsCode

Coming soon! Add your other apps here:
- App 1: [Link]
- App 2: [Link]
- App 3: [Link]

---

## 📞 Contact

**Developer**: KreggsCode  
**Email**: kreggscode@gmail.com  
**GitHub**: [github.com/kreggscode](https://github.com/kreggscode)  
**Play Store**: [View All Apps](https://play.google.com/store/apps/dev?id=YOUR_DEVELOPER_ID)

---

## 📄 License

Website design © 2025 KreggsCode. All rights reserved.

---

## 🎉 Quick Start Checklist

- [ ] Create GitHub repository
- [ ] Upload `docs` folder
- [ ] Enable GitHub Pages
- [ ] Add screenshots to `docs/screenshots/`
- [ ] Update Play Store link in HTML files
- [ ] Update developer ID in footer
- [ ] Add privacy policy URL to Play Console
- [ ] Add website URL to Play Console
- [ ] Test website on mobile and desktop
- [ ] Share with users!

---

**Your beautiful website is ready! 🚀**

Privacy Policy URL: `https://YOUR_USERNAME.github.io/morse-code-master/privacy.html`

Replace `YOUR_USERNAME` with your actual GitHub username.
